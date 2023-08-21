package online.partyrun.partyrunapplication.feature.my_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    navigationToProfile: () -> Unit = {},
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
        navigationToProfile = navigationToProfile,
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
    navigationToProfile: () -> Unit,
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
                    navigationToProfile = navigationToProfile
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
        navigationContent = {
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
    navigationToProfile: () -> Unit,
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
                navigationToProfile = navigationToProfile,
                userName = userData.name,
                userProfile = userData.profile
            )
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
    navigationToProfile: () -> Unit,
    userName: String,
    userProfile: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            navigationToProfile = navigationToProfile,
            userName = userName
        )
        ProfileImage(
            userProfile = userProfile
        )
    }
}

@Composable
private fun ProfileHeader(
    navigationToProfile: () -> Unit,
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
                navigationToProfile()
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
            .clip(CircleShape)
            .zIndex(1f)
    ) {
        RenderAsyncUrlImage(
            imageUrl = userProfile,
            contentDescription = null
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
