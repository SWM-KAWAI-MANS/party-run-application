package online.partyrun.partyrunapplication.core.model.running

import online.partyrun.partyrunapplication.core.model.util.formatPace
import java.time.Duration

data class RecordDataWithDistance(
    val records: List<GpsDataWithDistance>
)

fun RecordDataWithDistance.calculateInstantPace(): String {
    val (currentGpsData, lastGpsData) = getLastTwoGpsData() ?: return "0'00''"

    val distanceCoveredInKm = calculateDistanceInKm(currentGpsData, lastGpsData)
    val timeElapsedInSeconds = calculateDurationInSeconds(currentGpsData, lastGpsData)

    if (isInvalidData(distanceCoveredInKm, timeElapsedInSeconds)) return "0'00''"

    return formatPace(timeElapsedInSeconds / distanceCoveredInKm)
}

private fun isInvalidData(distance: Double, time: Double) =
    isDistanceTooSmall(distance) || isTimeOrDistanceZero(time, distance)


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
    return distance < 0.0003 // 0.3m
}

private fun isTimeOrDistanceZero(time: Double, distance: Double): Boolean {
    return time == 0.0 || distance == 0.0
}

private fun calculateDurationInSeconds(
    currentGpsData: GpsDataWithDistance,
    lastGpsData: GpsDataWithDistance
): Double {
    val duration = Duration.between(lastGpsData.time, currentGpsData.time)
    return duration.seconds + duration.nano / 1_000_000_000.0
}