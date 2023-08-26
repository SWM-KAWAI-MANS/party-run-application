package online.partyrun.partyrunapplication.feature.running_result

import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleResultUiModel

sealed class RunningResultUiState {
    object Loading : RunningResultUiState()

    data class Success(
        val battleResult: BattleResultUiModel = BattleResultUiModel()
    ) : RunningResultUiState()

    object LoadFailed : RunningResultUiState()
}
