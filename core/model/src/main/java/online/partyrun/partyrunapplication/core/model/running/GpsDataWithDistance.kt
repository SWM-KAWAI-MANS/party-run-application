package online.partyrun.partyrunapplication.core.model.running

import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerRecordUiModel
import java.time.LocalDateTime

data class GpsDataWithDistance(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val time: LocalDateTime = LocalDateTime.now(),
    val distance: Double
)

fun GpsDataWithDistance.toUiModel(): RunnerRecordUiModel {
    return RunnerRecordUiModel(
        altitude = this.altitude,
        latitude = this.latitude,
        longitude = this.longitude,
        time = this.time,
        distance = this.distance
    )
}
