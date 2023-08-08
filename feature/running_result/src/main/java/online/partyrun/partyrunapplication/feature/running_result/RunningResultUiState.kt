package online.partyrun.partyrunapplication.feature.running_result

import online.partyrun.partyrunapplication.core.model.running_result.BattleResult

sealed class RunningResultUiState {
    object Loading : RunningResultUiState()

    data class Success(
        val battleResult: BattleResult = BattleResult(
            battleRunnerStatus = emptyList(),
            startTime = "",
            targetDistance = 1000
        )
    ) : RunningResultUiState()

    object LoadFailed : RunningResultUiState()
}