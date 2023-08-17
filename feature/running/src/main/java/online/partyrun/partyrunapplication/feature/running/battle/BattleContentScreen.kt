package online.partyrun.partyrunapplication.feature.running.battle

import android.app.Activity
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
import androidx.compose.ui.graphics.Color
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
    navigateToBattleOnWebSocketError: () -> Unit = {},
    navigationToRunningResult: () -> Unit = {},
    viewModel: BattleContentViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit
) {
    val battleUiState by viewModel.battleUiState.collectAsStateWithLifecycle()
    val battleId by viewModel.battleId.collectAsStateWithLifecycle()

    RunningBackNavigationHandler() // 대결 중 BackPressed 수행 시 처리할 핸들러

    LaunchedEffect(battleId) {
        battleId?.let { id ->
            viewModel.startBattleStream(
                battleId = id,
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


    CheckStartTime(battleUiState, battleId, viewModel)

    Content(battleUiState, navigationToRunningResult)
}

@Composable
fun Content(
    battleUiState: BattleUiState,
    navigationToRunningResult: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (battleUiState.screenState) {
            is BattleScreenState.Ready -> BattleReadyScreen(isConnecting = battleUiState.isConnecting)
            is BattleScreenState.Running -> BattleRunningScreen(battleUiState = battleUiState)
            BattleScreenState.Finish -> FinishScreen()
        }
    }
}

@Composable
private fun CheckStartTime(
    battleUiState: BattleUiState,
    battleId: String?,
    viewModel: BattleContentViewModel,
) {
    when (battleUiState.timeRemaining) {
        in 1..5 -> CountdownDialog(timeRemaining = battleUiState.timeRemaining)
        0 -> battleId?.let {
            // 위치 업데이트 시작 및 정지 로직
            StartBattleRunning(battleId, viewModel)
        }
    }
}

@Composable
private fun StartBattleRunning(
    battleId: String,
    viewModel: BattleContentViewModel,
) {
    DisposableEffect(Unit) {
        viewModel.initBattleState() // 러너 데이터를 기반으로 BattleState를 초기화하고 시작
        battleId.let {
            viewModel.startLocationUpdates(battleId = it)
        }

        onDispose {
            viewModel.stopLocationUpdates()
        }
    }
}

@Composable
fun RunningBackNavigationHandler() {
    val context = LocalContext.current
    val activity = context as? Activity
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val openDialog = remember { mutableStateOf(false) }

    RunningExitConfirmationDialog(openDialog) {
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
                openDialog.value = true
            }
        }

        onBackPressedDispatcher?.addCallback(onBackPressedCallback)

        onDispose { onBackPressedCallback.remove() }
    }
}

@Composable
fun RunningExitConfirmationDialog(
    openDialog: MutableState<Boolean>,
    confirmExit: () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
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
                        confirmExit()
                        openDialog.value = false
                    },
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp)
                        .shadow(5.dp, shape = CircleShape),
                    contentColor = Color.White,
                    containerColor = Color.Unspecified
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
                        openDialog.value = false
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
