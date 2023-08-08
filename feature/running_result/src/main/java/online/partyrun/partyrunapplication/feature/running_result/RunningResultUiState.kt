package online.partyrun.partyrunapplication.feature.running_result

import online.partyrun.partyrunapplication.core.model.running_result.BattleResult

sealed class RunningResultUiState {
    object Loading : RunningResultUiState()

    data class Success(
        val battleResult: BattleResult = BattleResult(
            battleRunnerStatus = emptyList(),
            startTime = "",
            targetDistance = 1000,
            targetDistanceFormatted = "", // 쉼표로 형식화
            targetDistanceInKm = "", // km 단위로 형식화
            battleDate = "" // "x월 x일" 형식화
        )
    ) : RunningResultUiState()

    object LoadFailed : RunningResultUiState()
}
