package online.partyrun.partyrunapplication.feature.running.battle

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunOutlinedButton
import online.partyrun.partyrunapplication.core.ui.CountdownDialog
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.battle.finish.FinishScreen
import online.partyrun.partyrunapplication.feature.running.battle.ready.BattleReadyScreen
import online.partyrun.partyrunapplication.feature.running.battle.running.BattleRunningScreen

@Composable
fun BattleContentScreen(
    targetDistance: Int?,
    navigateToBattleOnWebSocketError: () -> Unit = {},
    navigationToRunningResult: () -> Unit = {},
    battleContentViewModel: BattleContentViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit
) {
    val battleUiState by battleContentViewModel.battleUiState.collectAsStateWithLifecycle()
    val battleId by battleContentViewModel.battleId.collectAsStateWithLifecycle()
    val battleContentSnackbarMessage by battleContentViewModel.snackbarMessage.collectAsStateWithLifecycle()
    val openRunningExitDialog = remember { mutableStateOf(false) }

    Content(
        targetDistance = targetDistance,
        navigateToBattleOnWebSocketError = navigateToBattleOnWebSocketError,
        navigationToRunningResult = navigationToRunningResult,
        battleContentViewModel = battleContentViewModel,
        battleUiState = battleUiState,
        battleId = battleId,
        openRunningExitDialog = openRunningExitDialog,
        battleContentSnackbarMessage = battleContentSnackbarMessage,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun Content(
    targetDistance: Int?,
    navigateToBattleOnWebSocketError: () -> Unit = {},
    navigationToRunningResult: () -> Unit = {},
    battleContentViewModel: BattleContentViewModel,
    battleUiState: BattleUiState,
    battleId: String?,
    openRunningExitDialog: MutableState<Boolean>,
    battleContentSnackbarMessage: String,
    onShowSnackbar: (String) -> Unit
) {
    // 사용자가 선택한 거리를 전달받아서 BattleUiState에 업데이트
    LaunchedEffect(targetDistance) {
        targetDistance?.let { distance ->
            battleContentViewModel.updateSelectedDistance(distance)
        }
    }

    // 대결 중 BackPressed 수행 시 처리할 핸들러
    RunningBackNavigationHandler(
        battleContentViewModel = battleContentViewModel,
        openRunningExitDialog = openRunningExitDialog,
    )

    LaunchedEffect(battleId) {
        battleId.let { id ->
            battleContentViewModel.startBattleStream(
                battleId = id ?: "",
                navigateToBattleOnWebSocketError = navigateToBattleOnWebSocketError
            )
        }
    }

    /**
     * 목표 거리에 도달했다면, 4초 대기 후 결과 스크린으로 이동
     */
    if (battleUiState.isFinished) {
        LaunchedEffect(Unit) {
            delay(4000)
            navigationToRunningResult()
        }
    }

    LaunchedEffect(battleContentSnackbarMessage) {
        if (battleContentSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(battleContentSnackbarMessage)
            battleContentViewModel.clearSnackbarMessage()
        }
    }

    CheckStartTime(battleUiState, battleId, battleContentViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (battleUiState.screenState) {
            is BattleScreenState.Ready -> BattleReadyScreen(isConnecting = battleUiState.isConnecting)
            is BattleScreenState.Running ->
                BattleRunningScreen(
                    battleUiState = battleUiState,
                    openRunningExitDialog = openRunningExitDialog
                )

            BattleScreenState.Finish -> FinishScreen()
        }
    }
}

@Composable
private fun CheckStartTime(
    battleUiState: BattleUiState,
    battleId: String?,
    battleContentViewModel: BattleContentViewModel,
) {
    when (battleUiState.timeRemaining) {
        in 1..5 -> CountdownDialog(timeRemaining = battleUiState.timeRemaining)
        0 -> battleId?.let {
            // 위치 업데이트 시작 및 정지 로직
            StartBattleRunning(battleId, battleContentViewModel)
        }
    }
}

@Composable
private fun StartBattleRunning(
    battleId: String,
    viewModel: BattleContentViewModel,
) {
    val context = LocalContext.current // LocalContext를 통해 컨텍스트에 접근

    DisposableEffect(Unit) {
        setOrDisposeBattleRunning(true, viewModel, battleId, context)

        onDispose {
            setOrDisposeBattleRunning(false, viewModel, battleId, context)
        }
    }
}

private fun setOrDisposeBattleRunning(
    isStarting: Boolean,
    viewModel: BattleContentViewModel,
    battleId: String,
    context: Context
) {
    if (isStarting) {
        viewModel.initBattleState() // 러너 데이터를 기반으로 BattleState를 초기화하고 시작
        viewModel.startLocationUpdates(battleId)
    } else {
        viewModel.stopLocationUpdates()
    }
    toggleRunningService(isStarting, context)
}

// Foreground Service 시작/중지
private fun toggleRunningService(isStarting: Boolean, context: Context) {
    val intent = Intent(context, RunningService::class.java)
    intent.action = if (isStarting) "StartRunning" else "StopRunning"
    context.startService(intent)
}


@Composable
fun RunningBackNavigationHandler(
    battleContentViewModel: BattleContentViewModel,
    openRunningExitDialog: MutableState<Boolean>
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    RunningExitConfirmationDialog(
        openRunningExitDialog = openRunningExitDialog,
    ) {
        battleContentViewModel.disposeSocketResources()
        // 액티비티 재시작
        activity?.let {
            val intent = it.intent
            it.startActivity(intent)
            it.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            it.finish()
            it.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    DisposableEffect(Unit) {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                openRunningExitDialog.value = true
            }
        }

        onBackPressedDispatcher?.addCallback(onBackPressedCallback)

        onDispose { onBackPressedCallback.remove() }
    }
}

@Composable
fun RunningExitConfirmationDialog(
    openRunningExitDialog: MutableState<Boolean>,
    confirmExit: () -> Unit
) {
    if (openRunningExitDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openRunningExitDialog.value = false
            },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.exit_dialog_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = stringResource(id = R.string.exit_dialog_subtitle),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            confirmButton = {
                PartyRunGradientButton(
                    onClick = {
                        openRunningExitDialog.value = false
                        confirmExit()
                    },
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp)
                        .shadow(5.dp, shape = CircleShape)
                ) {
                    Text(
                        text = stringResource(id = R.string.exit_dialog_confirm),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                PartyRunOutlinedButton(
                    onClick = {
                        openRunningExitDialog.value = false
                    },
                    shape = RoundedCornerShape(35.dp),
                    borderStrokeWidth = 5.dp,
                    modifier = Modifier
                        .height(50.dp)
                        .shadow(5.dp, shape = CircleShape),
                ) {
                    Text(
                        text = stringResource(id = R.string.exit_dialog_cancel),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )
    }
}
