package online.partyrun.partyrunapplication.feature.running.battle

import online.partyrun.partyrunapplication.core.model.battle.BattleStatus

data class BattleUiState(
    val isConnecting: Boolean = true,
    val screenState: BattleScreenState = BattleScreenState.Ready,
    val showConnectionError: Boolean = false,
    val battleState: BattleStatus = BattleStatus(),
    val timeRemaining: Int = -1,
)
