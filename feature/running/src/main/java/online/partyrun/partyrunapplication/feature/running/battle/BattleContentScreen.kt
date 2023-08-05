package online.partyrun.partyrunapplication.feature.running.battle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import online.partyrun.partyrunapplication.core.model.battle.RunnerIds
import online.partyrun.partyrunapplication.core.ui.CountdownDialog
import online.partyrun.partyrunapplication.feature.running.battle.finish.FinishScreen
import online.partyrun.partyrunapplication.feature.running.battle.ready.BattleReadyScreen
import online.partyrun.partyrunapplication.feature.running.battle.running.BattleRunningScreen

@Composable
fun BattleContentScreen(
    navigateToBattleOnWebSocketError: () -> Unit = {},
    navigationToRunningResult: () -> Unit = {},
    battleId: String? = "",
    runnerIds: RunnerIds,
    viewModel: BattleContentViewModel = hiltViewModel()
) {
    val battleUiState by viewModel.battleUiState.collectAsState()

    /**
     * 목표 거리에 도달했다면, 반투명의 대결 종료 레이어 스크린을 띄움.
     */
    if (battleUiState.isFinished) {
        FinishScreen()
    }

    LaunchedEffect(battleId) {
        battleId?.let {
            viewModel.startBattleStream(
                battleId = it,
                navigateToBattleOnWebSocketError = navigateToBattleOnWebSocketError
            )
        }
    }

    CheckStartTime(battleUiState, battleId, viewModel, runnerIds)

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
            is BattleScreenState.Running -> BattleRunningScreen(battleUiState, navigationToRunningResult)
        }
    }
}

@Composable
private fun CheckStartTime(
    battleUiState: BattleUiState,
    battleId: String?,
    viewModel: BattleContentViewModel,
    runnerIds: RunnerIds
) {
    when (battleUiState.timeRemaining) {
        in 1..5 -> CountdownDialog(timeRemaining = battleUiState.timeRemaining)
        0 -> battleId?.let {
            // 위치 업데이트 시작 및 정지 로직
            StartBattleRunning(battleId, viewModel, runnerIds)
        }
    }
}

@Composable
private fun StartBattleRunning(
    battleId: String?,
    viewModel: BattleContentViewModel,
    runnerIds: RunnerIds
) {
    DisposableEffect(Unit) {
        viewModel.initBattleState(runnerIds) // 러너 데이터를 기반으로 BattleState를 초기화하고 시작
        battleId?.let {
            viewModel.startLocationUpdates(battleId = it)
        }

        onDispose {
            viewModel.stopLocationUpdates()
        }
    }
}
