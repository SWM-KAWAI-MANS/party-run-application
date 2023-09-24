package online.partyrun.partyrunapplication.feature.party

import android.Manifest
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.SurfaceRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.ui.HeadLine
import online.partyrun.partyrunapplication.feature.party.join.PartyJoinDialog
import online.partyrun.partyrunapplication.feature.running.permission.CheckMultiplePermissions

private var lastClickTime = 0L
private const val DEBOUNCE_DURATION = 100  // 0.1 seconds

@Composable
fun PartyScreen(
    modifier: Modifier = Modifier,
    navigateToPartyRoom: (String, Boolean) -> Unit,
    partyViewModel: PartyViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit
) {
    val partyUiState by partyViewModel.partyUiState.collectAsState()
    val partySnackbarMessage by partyViewModel.snackbarMessage.collectAsState()

    Content(
        modifier = modifier,
        partyViewModel = partyViewModel,
        partyUiState = partyUiState,
        navigateToPartyRoom = navigateToPartyRoom,
        partySnackbarMessage = partySnackbarMessage,
        onShowSnackbar = onShowSnackbar
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    partyViewModel: PartyViewModel,
    partyUiState: PartyUiState,
    navigateToPartyRoom: (String, Boolean) -> Unit,
    partySnackbarMessage: String,
    onShowSnackbar: (String) -> Unit
) {
    val showPermissionDialog = remember { mutableStateOf(false) }

    val permissionsList = listOfNotNull(
        Manifest.permission.ACCESS_FINE_LOCATION,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else null
    )
    val permissionState = rememberMultiplePermissionsState(permissions = permissionsList)
    val showJoinDialog = remember { mutableStateOf(false) }

    HandlePermissionActions(
        permissionState = permissionState,
        showPermissionDialog = showPermissionDialog
    )

    if (showJoinDialog.value) {
        PartyJoinDialog(
            onDismissRequest = {
                showJoinDialog.value = false
            },
            partyViewModel = partyViewModel,
            navigateToPartyRoom = navigateToPartyRoom
        )
    }

    LaunchedEffect(partyUiState.partyCode) {
        if (partyUiState.partyCode.isNotEmpty()) {
            navigateToPartyRoom(partyUiState.partyCode, true) // 매니저 권한 부여 == true
        }
    }

    LaunchedEffect(partySnackbarMessage) {
        if (partySnackbarMessage.isNotEmpty()) {
            onShowSnackbar(partySnackbarMessage)
            partyViewModel.clearSnackbarMessage()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PartyHeadline()
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PartyRunGradientButton(
                onClick = {
                    handleJoinParty(
                        showJoinDialog = showJoinDialog,
                        permissionState = permissionState,
                        showPermissionDialog = showPermissionDialog
                    )
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = PartyRunIcons.PartyJoinIcon),
                        contentDescription = stringResource(id = R.string.ic_join_desc)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.join_party),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            SurfaceRoundedRect(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TrackImagePager { index ->
                            partyViewModel.setKmState(KmState.values()[index])
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        onClick = {
                            handleCreateParty(
                                permissionState = permissionState,
                                showPermissionDialog = showPermissionDialog,
                                partyViewModel = partyViewModel,
                                currentKmState = partyViewModel.kmState.value
                            )
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = PartyRunIcons.PartyCreationIcon),
                                contentDescription = stringResource(id = R.string.ic_creation_desc)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(id = R.string.create_party),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun PartyHeadline() {
    HeadLine(
        modifier = Modifier
    ) {
        Text(
            text = stringResource(id = R.string.party_head_line_1),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = stringResource(id = R.string.party_head_line_2),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TrackImagePager(
    setKmState: (Int) -> Unit
) {
    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            pageCount = KmState.values().size,
            state = pagerState,
            key = { KmState.values()[it] },
            pageSize = PageSize.Fill
        ) { index ->
            LaunchedEffect(pagerState.currentPage) { // 페이지가 완전히 넘어갔을 경우에만 setKmState
                if (index == pagerState.currentPage) {
                    setKmState(index)
                }
            }
            TrackImage(currentKmState = KmState.values()[index])
        }
        Spacer(modifier = Modifier.height(10.dp))
        // Add a page indicator
        Row(
            Modifier.weight(0.1f),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(KmState.values().size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
private fun TrackImage(currentKmState: KmState) {
    Image(
        painter = painterResource(id = currentKmState.imageRes),
        contentDescription = stringResource(id = R.string.track_image_desc)
    )
}

@OptIn(ExperimentalPermissionsApi::class)
private fun handleCreateParty(
    permissionState: MultiplePermissionsState,
    showPermissionDialog: MutableState<Boolean>,
    partyViewModel: PartyViewModel,
    currentKmState: KmState
) {
    if (shouldExecuteStartAction(permissionState)) {
        partyViewModel.createParty(
            RunningDistance(
                distance = currentKmState.toDistance()
            )
        )
    } else {
        showPermissionDialog.value = true
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun handleJoinParty(
    permissionState: MultiplePermissionsState,
    showPermissionDialog: MutableState<Boolean>,
    showJoinDialog: MutableState<Boolean>
) {
    if (shouldExecuteStartAction(permissionState)) {
        showJoinDialog.value = true
    } else {
        showPermissionDialog.value = true
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun shouldExecuteStartAction(
    permissionState: MultiplePermissionsState,
): Boolean {
    return permissionState.allPermissionsGranted &&
            isDebounced(System.currentTimeMillis())
}

fun isDebounced(currentTime: Long): Boolean {
    if (currentTime - lastClickTime > DEBOUNCE_DURATION) {
        lastClickTime = currentTime
        return true
    }
    return false
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HandlePermissionActions(
    permissionState: MultiplePermissionsState,
    showPermissionDialog: MutableState<Boolean>
) {
    if (showPermissionDialog.value) {
        CheckMultiplePermissions(
            permissionState = permissionState,
            onPermissionResult = { if (it) showPermissionDialog.value = false },
            showPermissionDialog = showPermissionDialog
        )
    }
}
