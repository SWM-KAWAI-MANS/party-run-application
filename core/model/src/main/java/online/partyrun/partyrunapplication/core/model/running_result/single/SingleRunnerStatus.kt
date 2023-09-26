package online.partyrun.partyrunapplication.core.model.running_result.single

import online.partyrun.partyrunapplication.core.model.running.RunningTime
import online.partyrun.partyrunapplication.core.model.running.toElapsedSeconds
import online.partyrun.partyrunapplication.core.model.running.toElapsedTimeString
import online.partyrun.partyrunapplication.core.model.running_result.common.RunnerStatus
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerStatusUiModel
import online.partyrun.partyrunapplication.core.model.util.calculateAltitudeOverTime
import online.partyrun.partyrunapplication.core.model.util.calculateAverageAltitude
import online.partyrun.partyrunapplication.core.model.util.calculateAveragePace
import online.partyrun.partyrunapplication.core.model.util.calculateDistanceOverTime
import online.partyrun.partyrunapplication.core.model.util.calculateCumulativePacePerMinute

data class SingleRunnerStatus(
    override val endTime: String = "",
    override val elapsedTime: String = "", // 총 달린 시간
    override val secondsElapsedTime: Long = 0,
    override val records: List<SingleRunnerRecord> = listOf()
) : RunnerStatus

fun SingleRunnerStatus.toUiModel(runningTime: RunningTime): RunnerStatusUiModel {
    val updatedSecondsElapsedTime = runningTime.toElapsedSeconds().toLong()
    val updatedRunnerStatus = this.copy(secondsElapsedTime = updatedSecondsElapsedTime)

    return RunnerStatusUiModel(
        endTime = this.endTime,
        elapsedTime = runningTime.toElapsedTimeString(),
        secondsElapsedTime = updatedSecondsElapsedTime,
        records = this.records.map { it.toUiModel() },
        averagePace = calculateAveragePace(updatedRunnerStatus),
        averageAltitude = calculateAverageAltitude(updatedRunnerStatus),
        pacePerMinute = calculateCumulativePacePerMinute(this.records),
        distanceOverTime = calculateDistanceOverTime(this.records),
        altitudeOvertime = calculateAltitudeOverTime(this.records)
    )
}
