package online.partyrun.partyrunapplication.feature.my_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.user.User
import online.partyrun.partyrunapplication.core.ui.ProfileSection

@Composable
fun MyPageScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onSignOut: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateBack: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    onShowSnackbar: (String) -> Unit
) {
    val myPageUiState by myPageViewModel.myPageUiState.collectAsStateWithLifecycle()
    val myPageSnackbarMessage by myPageViewModel.snackbarMessage.collectAsStateWithLifecycle()

    Content(
        myPageViewModel = myPageViewModel,
        myPageUiState = myPageUiState,
        onSignOut = onSignOut,
        navigateToSettings = navigateToSettings,
        navigateBack = navigateBack,
        navigateToProfile = navigateToProfile,
        onShowSnackbar = onShowSnackbar,
        myPageSnackbarMessage = myPageSnackbarMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    myPageViewModel: MyPageViewModel,
    myPageUiState: MyPageUiState,
    onSignOut: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateBack: () -> Unit,
    navigateToProfile: () -> Unit,
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
                navigateBack = navigateBack,
                navigateToSettings = navigateToSettings
            )
        }
    ) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (myPageUiState) {
                is MyPageUiState.Loading -> LoadingBody()
                is MyPageUiState.Success -> MyPageBody(
                    viewModel = myPageViewModel,
                    onSignOut = onSignOut,
                    userData = myPageUiState.user,
                    navigateToProfile = navigateToProfile
                )

                is MyPageUiState.LoadFailed -> LoadingBody()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyPageTopAppBar(
    modifier: Modifier,
    navigateBack: () -> Unit,
    navigateToSettings: () -> Unit
) {
    PartyRunTopAppBar(
        modifier = modifier,
        navigateToContent = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painterResource(id = PartyRunIcons.ArrowBackIos),
                    contentDescription = stringResource(id = R.string.arrow_back_desc)
                )
            }
        },
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
    viewModel: MyPageViewModel,
    onSignOut: () -> Unit,
    userData: User,
    navigateToProfile: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
        Spacer(modifier = Modifier.height(10.dp))
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
                    value = "10.9",
                    title = "총 달린거리"
                ) {
                    Image(
                        painter = painterResource(id = PartyRunIcons.Step),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                        contentDescription = "총 달린 거리"
                    )
                }
                StatusElement(
                    value = "5.55''",
                    title = "평균 페이스"
                ) {
                    Image(
                        painter = painterResource(id = PartyRunIcons.Pace),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                        contentDescription = "평균 페이스"
                    )
                }
                StatusElement(
                    value = "00:00",
                    title = "총 누적시간"
                ) {
                    Image(
                        painter = painterResource(id = PartyRunIcons.Schedule),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                        contentDescription = "총 누적시간"
                    )
                }
            }
        }

        SignOutButton(
            onClick = {
                viewModel.signOutFromGoogle()
                viewModel.saveAgreementState(isChecked = false)
                onSignOut()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ProfileContent(
    navigateToProfile: () -> Unit,
    userName: String,
    userProfile: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            navigateToProfile = navigateToProfile,
            userName = userName
        )
        ProfileImage(
            userProfile = userProfile
        )
    }
}

@Composable
private fun ProfileHeader(
    navigateToProfile: () -> Unit,
    userName: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(1f) // 이미지와 동일한 Z-축 위치에 배치
        )

        IconButton(
            onClick = {
                navigateToProfile()
            },
            modifier = Modifier
                .size(45.dp)
                .padding(end = 10.dp)
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(id = PartyRunIcons.edit),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = stringResource(id = R.string.edit_desc)
            )
        }
    }
}

@Composable
private fun ProfileImage(
    userProfile: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .clip(CircleShape)
            .border(3.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
            .zIndex(1f)
    ) {
        RenderAsyncUrlImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = userProfile,
            contentDescription = null
        )
    }
}

@Composable
private fun StatusElement(
    value: String,
    title: String,
    statusElementImage: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statusElementImage()
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun SignOutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onClick() }
        ) {
            Text(
                text = stringResource(id = R.string.sign_out_btn),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
