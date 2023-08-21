package online.partyrun.partyrunapplication.feature.my_page.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTextInput
import online.partyrun.partyrunapplication.core.designsystem.component.TextInputType
import online.partyrun.partyrunapplication.core.designsystem.component.addFocusCleaner
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.user.User
import online.partyrun.partyrunapplication.core.ui.ProfileSection
import online.partyrun.partyrunapplication.feature.my_page.MyPageUiState
import online.partyrun.partyrunapplication.feature.my_page.MyPageViewModel
import online.partyrun.partyrunapplication.feature.my_page.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {},
) {
    val myPageUiState by myPageViewModel.myPageUiState.collectAsStateWithLifecycle()
    val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Content(
        profileViewModel = profileViewModel,
        myPageUiState = myPageUiState,
        profileUiState = profileUiState,
        keyboardController = keyboardController,
        focusManager = focusManager,
        navigateBack = navigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    myPageUiState: MyPageUiState,
    profileUiState: ProfileUiState,
    keyboardController: SoftwareKeyboardController? = null,
    focusManager: FocusManager,
    navigateBack: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ProfileTopAppBar(
                modifier = modifier,
                navigateBack = navigateBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (myPageUiState is MyPageUiState.Success) {
                if (keyboardController != null) {
                    ProfileBody(
                        profileViewModel = profileViewModel,
                        userData = myPageUiState.user,
                        profileUiState = profileUiState,
                        keyboardController = keyboardController,
                        focusManager = focusManager
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopAppBar(
    modifier: Modifier,
    navigateBack: () -> Unit,
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
                text = stringResource(id = R.string.profile_title)
            )
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ProfileBody(
    profileViewModel: ProfileViewModel,
    userData: User,
    profileUiState: ProfileUiState,
    keyboardController: SoftwareKeyboardController,
    focusManager: FocusManager
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .addFocusCleaner(
                    keyboardController = keyboardController,
                    focusManager = focusManager
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Spacer(modifier = Modifier.heightIn(30.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                NickNameSection(keyboardController, focusManager, profileViewModel)
                NickNameSupportingText(profileUiState)
            }
        }
        // 스크린 위치에 상관없이 항상 바텀에 고정으로 보이는 컴포넌트
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 5.dp
        ) {
            FixedBottomNavigationSheet(
                profileViewModel = profileViewModel
            )
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun NickNameSection(
    keyboardController: SoftwareKeyboardController,
    focusManager: FocusManager,
    profileViewModel: ProfileViewModel
) {
    Text(
        text = stringResource(id = R.string.profile_nickname_title),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onPrimary
    )
    PartyRunTextInput(
        type = TextInputType.FIELD,
        placeholder = {
            Text(
                text = stringResource(id = R.string.profile_nickname_placeholder),
                style = MaterialTheme.typography.labelLarge,
            )
        },
        keyboardController = keyboardController,
        focusManager = focusManager
    ) { nickName ->
        profileViewModel.setNickName(nickName)
    }
}

@Composable
private fun FixedBottomNavigationSheet(
    profileViewModel: ProfileViewModel
) {
    Row(
        modifier = Modifier
            .heightIn(50.dp)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        PartyRunGradientButton(
            onClick = { profileViewModel.passAllConditions() }
        ) {
            Text(
                text = stringResource(id = R.string.profile_edit_completed),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun ProfileContent(
    userName: String,
    userProfile: String,
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
                .padding(top = 6.dp, bottom = 5.dp)
                .align(Alignment.Center)
                .zIndex(1f) // 이미지와 동일한 Z-축 위치에 배치
        )
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
private fun NickNameSupportingText(profileUiState: ProfileUiState) {
    if (profileUiState.nickNameError) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = PartyRunIcons.ErrorOutline),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = profileUiState.nickNameSupportingText,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}