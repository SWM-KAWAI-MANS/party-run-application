package online.partyrun.partyrunapplication.feature.battle

sealed class BattleMainUiState {
    object Loading : BattleMainUiState()

    object Success : BattleMainUiState()

    object LoadFailed : BattleMainUiState()

}
