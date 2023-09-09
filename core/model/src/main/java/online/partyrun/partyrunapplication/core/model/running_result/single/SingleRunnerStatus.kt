package online.partyrun.partyrunapplication.core.model.running_result.single

import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerStatusUiModel
import online.partyrun.partyrunapplication.core.model.util.formatDurationToTimeString
import online.partyrun.partyrunapplication.core.model.util.formatPace
import java.time.Duration
import kotlin.math.roundToInt

data class SingleRunnerStatus(
    val endTime: String = "",
    val elapsedTime: String = "", // 총 달린 시간
    val secondsElapsedTime: Long = 0,
    val records: List<SingleRunnerRecord> = listOf() // records 필드 추가
)

fun SingleRunnerStatus.toUiModel(): RunnerStatusUiModel {
    return RunnerStatusUiModel(
        endTime = this.endTime,
        elapsedTime = this.elapsedTime,
        secondsElapsedTime = this.secondsElapsedTime,
        records = this.records.map { it.toUiModel() },
        averagePace = calculateAveragePace(this),
        averageAltitude = calculateAverageAltitude(this),
        pacePerMinute = calculatePacePerMinute(
            this.records
        ),
        distanceOverTime = calculateDistanceOverTime(
            this.records
        ),
        altitudeOvertime = calculateAltitudeOverTime(
            this.records
        )
    )
}

fun calculateAveragePace(runnerStatus: SingleRunnerStatus): String {
    val seconds = runnerStatus.secondsElapsedTime.toDouble()
    val lastRecordDistance = runnerStatus.records.lastOrNull()?.distance ?: 0.0
    val distanceInKm = lastRecordDistance / 1000.0

    val pace = if (distanceInKm > 0) seconds / distanceInKm else 0.0

    return formatPace(pace)
}

fun calculateAverageAltitude(runnerStatus: SingleRunnerStatus): Double {
    return if (runnerStatus.records.isNotEmpty()) {
        (runnerStatus.records.sumOf { it.altitude } / runnerStatus.records.size)
            .roundToInt().toDouble()
    } else {
        0.0
    }
}

fun calculatePacePerMinute(records: List<SingleRunnerRecord>): List<Pair<String, Double>> {
    val startTime = records.first().time
    return records.drop(1).mapNotNull { record ->
        val timeElapsed = Duration.between(startTime, record.time)
        val currentDistanceInKm = record.distance / 1000.0

        if (!timeElapsed.isZero && currentDistanceInKm != 0.0) {
            val rawPaceInSeconds = timeElapsed.toSeconds().toDouble() / currentDistanceInKm
            val minutesPart = (rawPaceInSeconds / 60).toInt()
            val secondsPart = (rawPaceInSeconds % 60).toInt()

            val formattedPace = "$minutesPart.${secondsPart}"
            Pair(formatDurationToTimeString(timeElapsed), formattedPace.toDouble())
        } else {
            null
        }
    }
}

fun calculateDistanceOverTime(records: List<SingleRunnerRecord>): List<Pair<String, Double>> {
    val startTime = records.first().time

    return records.map { record ->
        val timeElapsed = Duration.between(startTime, record.time)
        Pair(formatDurationToTimeString(timeElapsed), record.distance)
    }
}

fun calculateAltitudeOverTime(records: List<SingleRunnerRecord>): List<Pair<String, Double>> {
    val startTime = records.first().time

    return records.map { record ->
        val timeElapsed = Duration.between(startTime, record.time)
        Pair(formatDurationToTimeString(timeElapsed), record.altitude.roundToInt().toDouble())
    }
}
