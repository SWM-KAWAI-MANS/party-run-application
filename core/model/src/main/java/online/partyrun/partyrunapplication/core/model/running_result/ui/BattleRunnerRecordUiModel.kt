package online.partyrun.partyrunapplication.core.model.running_result.ui

import java.time.LocalDateTime

data class BattleRunnerRecordUiModel(
    val altitude: Double,
    val latitude: Double,
    val longitude: Double,
    val time: LocalDateTime,
    val distance: Double
)
