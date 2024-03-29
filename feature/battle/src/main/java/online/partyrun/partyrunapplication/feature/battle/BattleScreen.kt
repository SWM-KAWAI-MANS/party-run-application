package online.partyrun.partyrunapplication.feature.battle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import online.partyrun.partyrunapplication.core.designsystem.component.BottomHalfOvalGradientShape
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunMatchButton
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.ui.BackgroundBlurImage
import online.partyrun.partyrunapplication.core.ui.HeadLine
import online.partyrun.partyrunapplication.core.ui.KmInfoCard
import online.partyrun.partyrunapplication.feature.match.MatchDialog
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.MatchViewModel
import online.partyrun.partyrunapplication.feature.running.permission.HandlePermissionActions
import online.partyrun.partyrunapplication.feature.running.permission.settingPermissionVariables

private var lastClickTime = 0L
private const val DEBOUNCE_DURATION = 100  // 0.1 seconds

@Composable
fun BattleScreen(
    modifier: Modifier = Modifier,
    battleViewModel: BattleViewModel = hiltViewModel(),
    matchViewModel: MatchViewModel = hiltViewModel(),
    navigateToBattleRunningWithDistance: (Int) -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    val battleUiState by battleViewModel.battleUiState.collectAsState()
    val matchUiState by matchViewModel.matchUiState.collectAsState()
    val battleSnackbarMessage by battleViewModel.snackbarMessage.collectAsStateWithLifecycle()
    val matchSnackbarMessage by matchViewModel.snackbarMessage.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        battleUiState = battleUiState,
        battleViewModel = battleViewModel,
        matchViewModel = matchViewModel,
        navigateToBattleRunningWithDistance = navigateToBattleRunningWithDistance,
        matchUiState = matchUiState,
        battleSnackbarMessage = battleSnackbarMessage,
        matchSnackbarMessage = matchSnackbarMessage,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    battleUiState: BattleUiState,
    battleViewModel: BattleViewModel,
    matchViewModel: MatchViewModel,
    navigateToBattleRunningWithDistance: (Int) -> Unit,
    matchUiState: MatchUiState,
    battleSnackbarMessage: String,
    matchSnackbarMessage: String,
    onShowSnackbar: (String) -> Unit,
) {
    if (matchUiState.isAllRunnersAccepted) {
        val currentKmState by battleViewModel.kmState.collectAsState()
        navigateToBattleRunningWithDistance(currentKmState.toDistance())
        matchViewModel.closeMatchDialog() // 다이얼로그를 닫고 초기화 수행
    }

    LaunchedEffect(battleSnackbarMessage) {
        if (battleSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(battleSnackbarMessage)
            battleViewModel.clearSnackbarMessage()
        }
    }

    LaunchedEffect(matchSnackbarMessage) {
        if (matchSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(matchSnackbarMessage)
            matchViewModel.clearSnackbarMessage()
        }
    }

    Box(modifier = modifier) {
        when (battleUiState) {
            is BattleUiState.Loading -> LoadingBody()
            is BattleUiState.Success -> BattleMainBody(
                battleViewModel = battleViewModel,
                matchViewModel = matchViewModel,
                matchUiState = matchUiState
            )

            is BattleUiState.LoadFailed -> LoadingBody()
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
fun BattleMainBody(
    battleViewModel: BattleViewModel,
    matchViewModel: MatchViewModel,
    matchUiState: MatchUiState
) {
    val (showPermissionDialog, permissionState) = settingPermissionVariables()

    HandlePermissionActions(
        permissionState = permissionState,
        showPermissionDialog = showPermissionDialog
    )

    if (matchUiState.isOpen) {
        MatchDialog(
            setShowDialog = {
                matchViewModel.closeMatchDialog()
            }
        )
    }

    BackgroundLayer()
    MainContent(
        battleViewModel,
        matchUiState,
        matchViewModel,
        permissionState,
        showPermissionDialog
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MainContent(
    battleViewModel: BattleViewModel,
    matchUiState: MatchUiState,
    matchViewModel: MatchViewModel,
    permissionState: MultiplePermissionsState,
    showPermissionDialog: MutableState<Boolean>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadLine(
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.battle_head_line_1),
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = stringResource(id = R.string.battle_head_line_2),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        KmInfoCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onLeftClick = {
                battleViewModel.onLeftKmChangeButtonClick()
            },
            onRightClick = {
                battleViewModel.onRightKmChangeButtonClick()
            }
        ) {
            TrackImage(battleViewModel)
        }
        Spacer(modifier = Modifier.weight(0.1f))
        PartyRunMatchButton(
            modifier = Modifier.padding(bottom = 70.dp),
            onClick = {
                if (shouldExecuteMatchingAction(permissionState, matchUiState)) {
                    matchingAction(matchViewModel, battleViewModel.kmState.value)
                } else {
                    matchViewModel.closeMatchDialog()
                    showPermissionDialog.value = true
                }
            }
        ) {
            Text(
                text = stringResource(id = R.string.battle_matching_start),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

/**
 * shouldExecuteMatchingAction 러닝이 가능한 상태인지에 대한 여부 파악
 * 1. 러닝을 위한 모든 권한이 부여됐는지 파악
 * 2. matchUiState.isMatchingBtnEnabled를 조건부로 처리 ->
 * enabled로 할 경우 버튼이 사라지는 현상 방지
 */
@OptIn(ExperimentalPermissionsApi::class)
private fun shouldExecuteMatchingAction(
    permissionState: MultiplePermissionsState,
    matchUiState: MatchUiState
): Boolean {
    return permissionState.allPermissionsGranted &&
            isDebounced(System.currentTimeMillis()) &&
            matchUiState.isMatchingBtnEnabled
}

@Composable
private fun BackgroundLayer() {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundBlurImage(
            modifier = Modifier.fillMaxSize(),
            image = R.drawable.backgroundblur,
            contentAlignment = Alignment.BottomCenter
        )
        BottomHalfOvalGradientShape(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(230.dp)
        )
    }
}

@Composable
private fun TrackImage(battleViewModel: BattleViewModel) {
    val currentKmState by battleViewModel.kmState.collectAsStateWithLifecycle()

    val imageRes = when (currentKmState) {
        KmState.KM_1 -> R.drawable.track_1km
        KmState.KM_3 -> R.drawable.track_3km
        KmState.KM_5 -> R.drawable.track_5km
        KmState.KM_10 -> R.drawable.track_10km
    }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = imageRes),
        contentDescription = stringResource(id = R.string.track_img_desc)
    )
}

private fun matchingAction(matchViewModel: MatchViewModel, currentKmState: KmState) {
    matchViewModel.setMatchingBtnEnabled(isEnabled = false)
    matchViewModel.openMatchDialog()
    matchViewModel.beginBattleMatchingProcess(
        RunningDistance(
            distance = currentKmState.toDistance()
        )
    )
}

fun isDebounced(currentTime: Long): Boolean {
    if (currentTime - lastClickTime > DEBOUNCE_DURATION) {
        lastClickTime = currentTime
        return true
    }
    return false
}
