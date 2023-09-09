package online.partyrun.partyrunapplication.feature.running_result

import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleResultUiModel

sealed class BattleResultUiState {
    object Loading : BattleResultUiState()

    data class Success(
        val battleResult: BattleResultUiModel = BattleResultUiModel()
    ) : BattleResultUiState()

    object LoadFailed : BattleResultUiState()
}
