package online.partyrun.partyrunapplication.feature.running.battle

import online.partyrun.partyrunapplication.core.model.battle.BattleState

data class BattleUiState(
    val isConnecting: Boolean = true,
    val showConnectionError: Boolean = false,
    val battleState: BattleState = BattleState(),
    val timeRemaining: Int = -1,
)
