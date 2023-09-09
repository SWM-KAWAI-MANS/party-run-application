package online.partyrun.partyrunapplication.core.model.running_result.battle

import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerRecordUiModel
import java.time.LocalDateTime

/**
 * 해당 러너가 뛴 각 기록에 대한 세부 정보
 * @param time: 해당 GPS가 찍힌 시간
 * @param distance: 해당 GPS까지의 달린 총 거리
 */
data class BattleRunnerRecord(
    val altitude: Double,
    val latitude: Double,
    val longitude: Double,
    val time: LocalDateTime,
    val distance: Double
)

fun BattleRunnerRecord.toUiModel(): RunnerRecordUiModel {
    return RunnerRecordUiModel(
        altitude = this.altitude,
        latitude = this.latitude,
        longitude = this.longitude,
        time = this.time,
        distance = this.distance
    )
}
