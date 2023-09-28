package online.partyrun.partyrunapplication.feature.my_page.profile

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import online.partyrun.partyrunapplication.core.designsystem.component.LottieImage
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

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ProfileScreen(
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navigateToMyPage: () -> Unit = {},
    onShowSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    val myPageUiState by myPageViewModel.myPageUiState.collectAsStateWithLifecycle()
    val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val profileSnackbarMessage by profileViewModel.snackbarMessage.collectAsStateWithLifecycle()
    val photoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { profileViewModel.handlePickedImage(context, it) }
        }

    Content(
        profileViewModel = profileViewModel,
        myPageUiState = myPageUiState,
        profileUiState = profileUiState,
        photoPicker = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        },
        navigateToMyPage = navigateToMyPage,
        onShowSnackbar = onShowSnackbar,
        profileSnackbarMessage = profileSnackbarMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    myPageUiState: MyPageUiState,
    profileUiState: ProfileUiState,
    photoPicker: () -> Unit,
    navigateToMyPage: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    profileSnackbarMessage: String
) {
    val updateProgressState by profileViewModel.updateProgressState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(profileSnackbarMessage) {
        if (profileSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(profileSnackbarMessage)
            profileViewModel.clearSnackbarMessage()
        }
    }

    if (profileUiState.isProfileUpdateSuccess) {
        navigateToMyPage()
    }

    if (updateProgressState) {
        ProgressIndicator()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ProfileTopAppBar(
                modifier = modifier,
                navigateToMyPage = navigateToMyPage
            )
        }
    ) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (myPageUiState is MyPageUiState.Success && keyboardController != null) {
                ProfileBody(
                    profileViewModel = profileViewModel,
                    userData = myPageUiState.user,
                    photoPicker = photoPicker,
                    profileUiState = profileUiState,
                    keyboardController = keyboardController,
                    focusManager = focusManager
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopAppBar(
    modifier: Modifier,
    navigateToMyPage: () -> Unit,
) {
    PartyRunTopAppBar(
        modifier = modifier,
        navigateToContent = {
            IconButton(onClick = { navigateToMyPage() }) {
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
    photoPicker: () -> Unit,
    profileUiState: ProfileUiState,
    keyboardController: SoftwareKeyboardController,
    focusManager: FocusManager
) {
    LaunchedEffect(Unit) {
        profileViewModel.initProfileContent(
            name = userData.nickName,
            profileImage = userData.profileImage
        )
    }

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
                    userName = profileUiState.nickName,
                    userProfile = profileUiState.profileImage,
                    photoPicker = photoPicker
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
private fun ProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures { } // progress 중에는 터치 블락
            },
        contentAlignment = Alignment.Center
    ) {
        LottieImage(modifier = Modifier.size(60.dp), rawAnimation = R.raw.mypage_progress)
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
            onClick = { profileViewModel.updateUserData() }
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
    photoPicker: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            userName = userName
        )
        ProfileImage(
            userProfile = userProfile,
            photoPicker = photoPicker
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
    userProfile: String,
    photoPicker: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                .clickable {
                    photoPicker()
                }
        ) {
            RenderAsyncUrlImage(
                modifier = Modifier.fillMaxSize(),
                imageUrl = userProfile,
                contentDescription = stringResource(id = R.string.profile_img_desc)
            )
        }
        Icon(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.onPrimary)
                .size(24.dp),
            painter = painterResource(id = PartyRunIcons.AddCircleFilled),
            tint = MaterialTheme.colorScheme.primary,
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
                contentDescription = stringResource(id = R.string.profile_alarm_icon_desc),
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
