package online.partyrun.partyrunapplication.core.model.running_result.common

import java.time.LocalDateTime

interface RunnerRecord {
    val altitude: Double
    val latitude: Double
    val longitude: Double
    val time: LocalDateTime
    val distance: Double
}
