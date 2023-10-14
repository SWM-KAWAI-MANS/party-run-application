package online.partyrun.partyrunapplication.core.model.util

import online.partyrun.partyrunapplication.core.model.running_result.common.RunnerRecord
import online.partyrun.partyrunapplication.core.model.running_result.common.RunnerStatus
import java.time.Duration
import kotlin.math.roundToInt

fun formatPace(pace: Double): String {
    val minutesPart = (pace / 60).toInt()
    val secondsPart = (pace % 60).toInt()

    // %02d는 정수를 두 자리로 표현하는데, 만약 한 자리수면 앞에 0 추가
    return "${minutesPart}'${String.format("%02d", secondsPart)}''"
}

fun formatDurationToTimeString(duration: Duration): String {
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}


fun calculateAveragePace(runnerStatus: RunnerStatus): String {
    val seconds = runnerStatus.secondsElapsedTime.toDouble()
    val lastRecordDistance = runnerStatus.records.lastOrNull()?.distance ?: 0.0
    val distanceInKm = lastRecordDistance / 1000.0

    val pace = if (distanceInKm > 0) seconds / distanceInKm else 0.0

    return formatPace(pace)
}

fun calculatePaceInMinPerKm(speedInMetersPerSec: Double): String {
    if (speedInMetersPerSec == 0.0) {
        return "0'00''"
    }

    val paceInMinPerKm = (1 / speedInMetersPerSec) * (1000 / 60)
    val minutes = paceInMinPerKm.toInt()
    val seconds = ((paceInMinPerKm - minutes) * 60).roundToInt()

    // %02d는 정수를 두 자리로 표현하는데, 만약 한 자리수면 앞에 0 추가
    return "${minutes}'${String.format("%02d", seconds)}''"
}

fun calculateAverageAltitude(runnerStatus: RunnerStatus): Double {
    return if (runnerStatus.records.isNotEmpty()) {
        (runnerStatus.records.sumOf { it.altitude } / runnerStatus.records.size)
            .roundToInt().toDouble()
    } else {
        0.0
    }
}

fun calculateCumulativePacePerMinute(records: List<RunnerRecord>): List<Pair<String, Double>> {
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

fun calculateDistanceOverTime(records: List<RunnerRecord>): List<Pair<String, Double>> {
    val startTime = records.first().time

    return records.map { record ->
        val timeElapsed = Duration.between(startTime, record.time)
        Pair(formatDurationToTimeString(timeElapsed), record.distance)
    }
}

fun calculateAltitudeOverTime(records: List<RunnerRecord>): List<Pair<String, Double>> {
    val startTime = records.first().time

    return records.map { record ->
        val timeElapsed = Duration.between(startTime, record.time)
        Pair(formatDurationToTimeString(timeElapsed), record.altitude.roundToInt().toDouble())
    }
}
