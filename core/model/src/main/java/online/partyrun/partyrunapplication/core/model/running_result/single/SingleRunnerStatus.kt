package online.partyrun.partyrunapplication.core.model.running_result.single

import online.partyrun.partyrunapplication.core.model.running_result.common.RunnerStatus
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerStatusUiModel
import online.partyrun.partyrunapplication.core.model.util.calculateAltitudeOverTime
import online.partyrun.partyrunapplication.core.model.util.calculateAverageAltitude
import online.partyrun.partyrunapplication.core.model.util.calculateAveragePace
import online.partyrun.partyrunapplication.core.model.util.calculateDistanceOverTime
import online.partyrun.partyrunapplication.core.model.util.calculatePacePerMinute

data class SingleRunnerStatus(
    override val endTime: String = "",
    override val elapsedTime: String = "", // 총 달린 시간
    override val secondsElapsedTime: Long = 0,
    override val records: List<SingleRunnerRecord> = listOf() // records 필드 추가
) : RunnerStatus

fun SingleRunnerStatus.toUiModel(): RunnerStatusUiModel {
    return RunnerStatusUiModel(
        endTime = this.endTime,
        elapsedTime = this.elapsedTime,
        secondsElapsedTime = this.secondsElapsedTime,
        records = this.records.map { it.toUiModel() },
        averagePace = calculateAveragePace(this),
        averageAltitude = calculateAverageAltitude(this),
        pacePerMinute = calculatePacePerMinute(this.records),
        distanceOverTime = calculateDistanceOverTime(this.records),
        altitudeOvertime = calculateAltitudeOverTime(this.records)
    )
}
