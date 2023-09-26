package online.partyrun.partyrunapplication.core.model.running

import online.partyrun.partyrunapplication.core.model.util.formatPace
import java.time.Duration

data class RecordDataWithDistance(
    val records: List<GpsDataWithDistance>
)

fun RecordDataWithDistance.calculateInstantPace(): String? {
    val (currentGpsData, lastGpsData) = getLastTwoGpsData() ?: return null

    val distanceCoveredInKm = calculateDistanceInKm(currentGpsData, lastGpsData)
    val timeElapsedInSeconds = calculateDurationInSeconds(currentGpsData, lastGpsData)

    if (isTimeOrDistanceZero(distanceCoveredInKm, timeElapsedInSeconds)) return "0'00''"
    if (isDistanceTooSmall(distanceCoveredInKm)) return null

    return formatPace(timeElapsedInSeconds / distanceCoveredInKm)
}

private fun RecordDataWithDistance.getLastTwoGpsData(): Pair<GpsDataWithDistance, GpsDataWithDistance>? {
    val currentGpsData = records.lastOrNull() ?: return null
    val lastGpsData = if (records.size > 1) records[records.size - 2] else return null
    return currentGpsData to lastGpsData
}

private fun calculateDistanceInKm(
    currentGpsData: GpsDataWithDistance,
    lastGpsData: GpsDataWithDistance
): Double {
    return (currentGpsData.distance - lastGpsData.distance) / 1000.0
}

private fun isDistanceTooSmall(distance: Double): Boolean {
    return distance < 0.0008 // 0.8m
}

private fun isTimeOrDistanceZero(distance: Double, time: Double): Boolean {
    return time == 0.0 || distance == 0.0
}

private fun calculateDurationInSeconds(
    currentGpsData: GpsDataWithDistance,
    lastGpsData: GpsDataWithDistance
): Double {
    val duration = Duration.between(lastGpsData.time, currentGpsData.time)
    return duration.seconds + duration.nano / 1_000_000_000.0
}