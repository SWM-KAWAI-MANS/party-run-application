package online.partyrun.partyrunapplication.feature.single

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.SurfaceRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.ui.HeadLine
import online.partyrun.partyrunapplication.feature.running.permission.CheckMultiplePermissions

private var lastClickTime = 0L
private const val DEBOUNCE_DURATION = 100  // 0.1 seconds

@Composable
fun SingleScreen(
    modifier: Modifier = Modifier,
    navigateToSingleRunningWithDistanceAndTime: (Int, Int) -> Unit,
    singleViewModel: SingleViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit
) {
    val singleUiState by singleViewModel.singleUiState.collectAsStateWithLifecycle()
    val singleSnackbarMessage by singleViewModel.snackbarMessage.collectAsStateWithLifecycle()
    val targetDistance by singleViewModel.targetDistance.collectAsStateWithLifecycle()
    val targetTime by singleViewModel.targetTime.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        singleUiState = singleUiState,
        singleViewModel = singleViewModel,
        targetDistance = targetDistance,
        targetTime = targetTime,
        navigateToSingleRunningWithDistanceAndTime = navigateToSingleRunningWithDistanceAndTime,
        singleSnackbarMessage = singleSnackbarMessage,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    singleUiState: SingleUiState,
    singleViewModel: SingleViewModel,
    targetDistance: Int,
    targetTime: Int,
    navigateToSingleRunningWithDistanceAndTime: (Int, Int) -> Unit,
    singleSnackbarMessage: String,
    onShowSnackbar: (String) -> Unit
) {
    LaunchedEffect(singleSnackbarMessage) {
        if (singleSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(singleSnackbarMessage)
            singleViewModel.clearSnackbarMessage()
        }
    }

    Box(modifier = modifier) {
        when (singleUiState) {
            is SingleUiState.Loading -> LoadingBody()
            is SingleUiState.Success -> SingleMainBody(
                singleViewModel = singleViewModel,
                targetDistance = targetDistance,
                targetTime = targetTime,
                navigateToSingleRunningWithDistanceAndTime = navigateToSingleRunningWithDistanceAndTime
            )

            is SingleUiState.LoadFailed -> LoadingBody()
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SingleMainBody(
    singleViewModel: SingleViewModel,
    targetDistance: Int,
    targetTime: Int,
    navigateToSingleRunningWithDistanceAndTime: (Int, Int) -> Unit
) {
    val showPermissionDialog = remember { mutableStateOf(false) }

    val permissionsList = listOfNotNull(
        Manifest.permission.ACCESS_FINE_LOCATION,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else null
    )
    val permissionState = rememberMultiplePermissionsState(permissions = permissionsList)

    HandlePermissionActions(
        permissionState = permissionState,
        showPermissionDialog = showPermissionDialog
    )

    SingleContent(
        singleViewModel = singleViewModel,
        permissionState = permissionState,
        showPermissionDialog = showPermissionDialog,
        targetDistance = targetDistance,
        targetTime = targetTime,
        navigateToSingleRunningWithDistanceAndTime = navigateToSingleRunningWithDistanceAndTime
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SingleContent(
    singleViewModel: SingleViewModel,
    permissionState: MultiplePermissionsState,
    showPermissionDialog: MutableState<Boolean>,
    targetDistance: Int,
    targetTime: Int,
    navigateToSingleRunningWithDistanceAndTime: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadLine(
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.single_headline_1),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = stringResource(id = R.string.single_headline_2),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        SurfaceRoundedRect(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TargetDistanceSetting(
                        singleViewModel = singleViewModel
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TargetTimeSetting(
                        singleViewModel = singleViewModel
                    )
                }
                PartyRunGradientButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    onClick = {
                        handleStartButtonClick(
                            permissionState,
                            navigateToSingleRunningWithDistanceAndTime,
                            targetDistance,
                            targetTime,
                            showPermissionDialog
                        )
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = stringResource(R.string.single_start),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(50.dp))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun handleStartButtonClick(
    permissionState: MultiplePermissionsState,
    navigateToSingleRunningWithDistanceAndTime: (Int, Int) -> Unit,
    targetDistance: Int,
    targetTime: Int,
    showPermissionDialog: MutableState<Boolean>
) {
    if (shouldExecuteStartAction(permissionState)) {
        navigateToSingleRunningWithDistanceAndTime(
            targetDistance,
            targetTime
        )
    } else {
        showPermissionDialog.value = true
    }
}

@Composable
private fun TargetDistanceSetting(
    singleViewModel: SingleViewModel
) {
    val displayTargetDistance = singleViewModel.getFormattedTargetDistance()

    TargetSettingRow(
        title = stringResource(id = R.string.target_distance),
        displayValue = displayTargetDistance,
        unit = stringResource(id = R.string.target_distance_unit),
        onIncrement = { singleViewModel.incrementTargetDistance() },
        onDecrement = { singleViewModel.decrementTargetDistance() }
    )
}

@Composable
private fun TargetTimeSetting(
    singleViewModel: SingleViewModel
) {
    val displayTargetTime = singleViewModel.getFormattedTargetTime()

    TargetSettingRow(
        title = stringResource(id = R.string.target_time),
        displayValue = displayTargetTime,
        unit = stringResource(id = R.string.target_time_unit),
        onIncrement = { singleViewModel.incrementTargetTime() },
        onDecrement = { singleViewModel.decrementTargetTime() }
    )
}

@Composable
private fun TargetSettingRow(
    modifier: Modifier = Modifier,
    title: String,
    displayValue: String,
    unit: String,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onDecrement() }
                .size(45.dp),
            painter = painterResource(id = PartyRunIcons.RemoveCircleFilled),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = stringResource(id = R.string.decrement_btn_desc)
        )
        Row(
            modifier = Modifier
        ) {
            Text(
                text = displayValue,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(bottom = 10.dp),
                text = unit,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onIncrement() }
                .size(45.dp),
            painter = painterResource(id = PartyRunIcons.AddCircleFilled),
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = stringResource(id = R.string.increment_btn_desc)
        )
    }
}

/**
 * shouldExecuteStartAction 러닝이 가능한 상태인지에 대한 여부 파악
 * 러닝을 위한 모든 권한이 부여됐는지 파악
 */
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
