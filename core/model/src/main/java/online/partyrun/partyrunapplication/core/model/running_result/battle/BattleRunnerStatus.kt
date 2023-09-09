package online.partyrun.partyrunapplication.core.model.running_result.battle

import online.partyrun.partyrunapplication.core.model.running_result.common.RunnerStatus
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerStatusUiModel
import online.partyrun.partyrunapplication.core.model.util.calculateAltitudeOverTime
import online.partyrun.partyrunapplication.core.model.util.calculateAverageAltitude
import online.partyrun.partyrunapplication.core.model.util.calculateAveragePace
import online.partyrun.partyrunapplication.core.model.util.calculateDistanceOverTime
import online.partyrun.partyrunapplication.core.model.util.calculatePacePerMinute

data class BattleRunnerStatus(
    val id: String = "", // 해당 Runner ID
    val name: String = "",
    val profile: String = "",
    val rank: Int = 0,
    override val endTime: String = "",
    override val secondsElapsedTime: Long = 0,
    override val elapsedTime: String = "", // 총 달린 시간
    override val records: List<BattleRunnerRecord> = listOf() // records 필드 추가
) : RunnerStatus

fun BattleRunnerStatus.toUiModel(): RunnerStatusUiModel {
    return RunnerStatusUiModel(
        endTime = this.endTime,
        id = this.id,
        name = this.name,
        elapsedTime = this.elapsedTime,
        secondsElapsedTime = this.secondsElapsedTime,
        profile = this.profile,
        rank = this.rank,
        records = this.records.map { it.toUiModel() },
        averagePace = calculateAveragePace(this),
        averageAltitude = calculateAverageAltitude(this),
        pacePerMinute = calculatePacePerMinute(this.records),
        distanceOverTime = calculateDistanceOverTime(this.records),
        altitudeOvertime = calculateAltitudeOverTime(this.records)
    )
}
