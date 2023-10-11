package online.partyrun.partyrunapplication.feature.my_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.my_page.SingleRunningHistory
import online.partyrun.partyrunapplication.core.model.user.User
import online.partyrun.partyrunapplication.core.ui.ProfileSection
import online.partyrun.partyrunapplication.feature.my_page.component.EmptyRunningHistory
import online.partyrun.partyrunapplication.feature.my_page.component.ProfileContent
import online.partyrun.partyrunapplication.feature.my_page.component.RunningDataCard
import online.partyrun.partyrunapplication.feature.my_page.component.ShimmerRunningHistory
import online.partyrun.partyrunapplication.feature.my_page.component.ShimmerStatusElement
import online.partyrun.partyrunapplication.feature.my_page.component.StatusElement

@Composable
fun MyPageScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    navigateToSingleResult: (Boolean) -> Unit = {},
    onShowSnackbar: (String) -> Unit
) {
    val myPageProfileState by myPageViewModel.myPageProfileState.collectAsStateWithLifecycle()
    val myPageSnackbarMessage by myPageViewModel.snackbarMessage.collectAsStateWithLifecycle()

    Content(
        myPageViewModel = myPageViewModel,
        myPageProfileState = myPageProfileState,
        navigateToSettings = navigateToSettings,
        navigateToProfile = navigateToProfile,
        navigateToSingleResult = navigateToSingleResult,
        onShowSnackbar = onShowSnackbar,
        myPageSnackbarMessage = myPageSnackbarMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    myPageViewModel: MyPageViewModel,
    myPageProfileState: MyPageProfileState,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSingleResult: (Boolean) -> Unit,
    onShowSnackbar: (String) -> Unit,
    myPageSnackbarMessage: String
) {
    LaunchedEffect(myPageSnackbarMessage) {
        if (myPageSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(myPageSnackbarMessage)
            myPageViewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            MyPageTopAppBar(
                modifier = modifier,
                navigateToSettings = navigateToSettings
            )
        }
    ) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (myPageProfileState) {
                is MyPageProfileState.Loading -> LoadingBody()
                is MyPageProfileState.Success -> MyPageBody(
                    userData = myPageProfileState.user,
                    myPageViewModel = myPageViewModel,
                    navigateToProfile = navigateToProfile,
                    navigateToSingleResult = navigateToSingleResult
                )

                is MyPageProfileState.LoadFailed -> LoadingBody()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyPageTopAppBar(
    modifier: Modifier,
    navigateToSettings: () -> Unit
) {
    PartyRunTopAppBar(
        modifier = modifier,
        titleContent = {
            Text(
                text = stringResource(id = R.string.my_page_title)
            )
        },
        actionsContent = {
            IconButton(onClick = { navigateToSettings() }) {
                Icon(
                    painterResource(id = PartyRunIcons.Settings),
                    contentDescription = stringResource(id = R.string.setting_desc)
                )
            }
        }
    )
}

@Composable
private fun LoadingBody() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MyPageBody(
    userData: User,
    myPageViewModel: MyPageViewModel,
    navigateToProfile: () -> Unit,
    navigateToSingleResult: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        myPageViewModel.getComprehensiveRunRecord()
        myPageViewModel.getSingleRunningHistory()
    }

    LaunchedEffect(Unit) {// 러닝 기록 상세 보기 클릭
        myPageViewModel.saveIdCompleteEvent.collect { modeType ->
            when (modeType) {
                ModeType.SINGLE -> navigateToSingleResult(true) // MyPage로부터 Result로의 이동은 true
                else -> {
                    // Battle
                }
            }
        }
    }

    val myPageComprehensiveRunRecordState by myPageViewModel.myPageComprehensiveRunRecordState.collectAsStateWithLifecycle()
    val runningHistoryState by myPageViewModel.runningHistoryState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        ProfileSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .heightIn(max = max(270.dp, with(LocalDensity.current) { 200.sp.toDp() }))
        ) {
            ProfileContent(
                navigateToProfile = navigateToProfile,
                userName = userData.nickName,
                userProfile = userData.profileImage
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        ComprehensiveRunRecordRect(
            myPageComprehensiveRunRecordState = myPageComprehensiveRunRecordState
        )

        Spacer(modifier = Modifier.height(30.dp))
        SingleRunningHistoryRow(
            runningHistoryState = runningHistoryState,
            myPageViewModel = myPageViewModel
        )

        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.battle_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(30.dp))

            EmptyRunningHistory()

        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun SingleRunningHistoryRow(
    runningHistoryState: RunningHistoryState,
    myPageViewModel: MyPageViewModel
) {
    when (runningHistoryState) {
        is RunningHistoryState.Loading -> ShimmerRunningHistory(isSingleData = true)
        is RunningHistoryState.Success -> SingleRunningHistoryBody(
            singleRunningHistory = runningHistoryState.singleRunningHistory,
            myPageViewModel = myPageViewModel
        )

        RunningHistoryState.LoadFailed -> ShimmerRunningHistory(isSingleData = true)
    }
}

@Composable
private fun SingleRunningHistoryBody(
    singleRunningHistory: SingleRunningHistory,
    myPageViewModel: MyPageViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.single_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(30.dp))

        if (singleRunningHistory.history.isEmpty()) {
            EmptyRunningHistory()
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(singleRunningHistory.history) { data ->
                    RunningDataCard(
                        runningHistoryDetail = data,
                        isSingleData = true
                    ) {
                        myPageViewModel.saveSingleId(data.id)
                    }
                }
            }
        }
    }
}

@Composable
private fun ComprehensiveRunRecordRect(
    myPageComprehensiveRunRecordState: MyPageComprehensiveRunRecordState
) {
    when (myPageComprehensiveRunRecordState) {
        is MyPageComprehensiveRunRecordState.Loading -> ShimmerComprehensiveRunRecordRect()
        is MyPageComprehensiveRunRecordState.Success -> ComprehensiveRunRecordRectBody(
            averagePace = myPageComprehensiveRunRecordState.comprehensiveRunRecord.averagePace,
            totalDistance = myPageComprehensiveRunRecordState.comprehensiveRunRecord.totalDistance,
            totalRunningTime = myPageComprehensiveRunRecordState.comprehensiveRunRecord.totalRunningTime
        )

        MyPageComprehensiveRunRecordState.LoadFailed -> ShimmerComprehensiveRunRecordRect()
    }
}

@Composable
private fun ShimmerComprehensiveRunRecordRect() {
    PartyRunGradientRoundedRect(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        cornerRadius = 20.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ShimmerStatusElement()
            ShimmerStatusElement()
            ShimmerStatusElement()
        }
    }
}

@Composable
private fun ComprehensiveRunRecordRectBody(
    averagePace: String,
    totalDistance: String,
    totalRunningTime: String
) {
    PartyRunGradientRoundedRect(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        cornerRadius = 20.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatusElement(
                value = totalDistance,
                title = stringResource(id = R.string.total_distance)
            ) {
                Image(
                    painter = painterResource(id = PartyRunIcons.Step),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    contentDescription = stringResource(id = R.string.total_distance_desc)
                )
            }
            StatusElement(
                value = averagePace,
                title = stringResource(id = R.string.avg_pace)
            ) {
                Image(
                    painter = painterResource(id = PartyRunIcons.Pace),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    contentDescription = stringResource(id = R.string.avg_pace_desc)
                )
            }
            StatusElement(
                value = totalRunningTime,
                title = stringResource(id = R.string.total_accumulated_time)
            ) {
                Image(
                    painter = painterResource(id = PartyRunIcons.Schedule),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    contentDescription = stringResource(id = R.string.total_accumulated_time_desc)
                )
            }
        }
    }
}
