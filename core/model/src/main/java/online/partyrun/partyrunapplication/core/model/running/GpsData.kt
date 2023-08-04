package online.partyrun.partyrunapplication.core.model.running

import java.time.LocalDateTime

data class GpsData(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val time: LocalDateTime = LocalDateTime.now()
)
