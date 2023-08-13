package online.partyrun.partyrunapplication.feature.my_page

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.user.User
import online.partyrun.partyrunapplication.core.ui.ProfileSection

@Composable
fun MyPageScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onSignOut: () -> Unit = {}
) {
    val myPageUiState by myPageViewModel.myPageUiState.collectAsStateWithLifecycle()

    Content(
        myPageViewModel = myPageViewModel,
        myPageUiState = myPageUiState,
        onSignOut = onSignOut
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    myPageViewModel: MyPageViewModel,
    myPageUiState: MyPageUiState,
    onSignOut: () -> Unit
) {
    Box(modifier = modifier) {
        when (myPageUiState) {
            is MyPageUiState.Loading -> LoadingBody()
            is MyPageUiState.Success -> MyPageBody(viewModel = myPageViewModel, onSignOut = onSignOut, userData = myPageUiState.user)
            is MyPageUiState.LoadFailed -> LoadingBody()
        }
    }
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
    userData: User
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        TopAppTitle(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(30.dp))

        ProfileSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .heightIn(max = max(270.dp, with(LocalDensity.current) { 200.sp.toDp() }))
        ) {
            ProfileContent(
                userName = userData.name,
                userProfile = userData.profile
            )
        }

        SignOutButton(
            onSignOut = {
                viewModel.signOutFromGoogle()
                onSignOut()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TopAppTitle(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.my_page_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
@Composable
private fun ProfileContent(
    userName: String,
    userProfile: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            userName = userName
        )
        ProfileImage(
            userProfile = userProfile
        )
    }
}

@Composable
private fun ProfileHeader(
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
            onClick = { /*TODO:*/ },
            modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(id = PartyRunIcons.Settings),
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = stringResource(id = R.string.setting_desc)
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
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onSignOut() }
        ) {
            Text(
                text = stringResource(id = R.string.sign_out_btn),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
