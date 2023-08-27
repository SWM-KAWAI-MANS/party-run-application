package online.partyrun.partyrunapplication.core.model.running_result

import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleRunnerStatusUiModel
import kotlin.math.roundToInt

data class BattleRunnerStatus(
    val endTime: String = "",
    val id: String = "", // 해당 Runner ID
    val name: String = "",
    val elapsedTime: String = "", // 총 달린 시간
    val secondsElapsedTime: Long = 0,
    val profile: String = "",
    val rank: Int = 0,
    val records: List<BattleRunnerRecord> = listOf() // records 필드 추가
)

fun BattleRunnerStatus.toUiModel(): BattleRunnerStatusUiModel {
    return BattleRunnerStatusUiModel(
        endTime = this.endTime,
        id = this.id,
        name = this.name,
        elapsedTime = this.elapsedTime,
        secondsElapsedTime = this.secondsElapsedTime,
        profile = this.profile,
        rank = this.rank,
        records = this.records.map { it.toUiModel() },
        averagePace = calculateAveragePace(this),
        averageAltitude = calculateAverageAltitude(this)
    )
}

fun calculateAveragePace(runnerStatus: BattleRunnerStatus): String {
    val seconds = runnerStatus.secondsElapsedTime.toDouble()
    val lastRecordDistance = runnerStatus.records.lastOrNull()?.distance ?: 0.0
    val distanceInKm = lastRecordDistance / 1000.0

    val pace = if (distanceInKm > 0) seconds / distanceInKm else 0.0

    return formatPace(pace)
}

private fun formatPace(pace: Double): String {
    val minutesPart = (pace / 60).toInt()
    val secondsPart = (pace % 60).roundToInt()

    // %02d는 정수를 두 자리로 표현하는데, 만약 한 자리수면 앞에 0 추가
    return "${minutesPart}'${String.format("%02d", secondsPart)}''"
}

fun calculateAverageAltitude(runnerStatus: BattleRunnerStatus): Double {
    return if (runnerStatus.records.isNotEmpty()) {
        runnerStatus.records.sumOf { it.altitude } / runnerStatus.records.size
    } else {
        0.0
    }
}
