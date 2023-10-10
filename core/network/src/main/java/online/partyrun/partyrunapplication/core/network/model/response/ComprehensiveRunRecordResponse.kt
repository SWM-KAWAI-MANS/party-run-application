package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.my_page.ComprehensiveRunRecord
import online.partyrun.partyrunapplication.core.model.my_page.TotalRunningTime
import online.partyrun.partyrunapplication.core.model.my_page.toElapsedTimeString
import online.partyrun.partyrunapplication.core.model.util.formatPace
import online.partyrun.partyrunapplication.core.network.model.util.formatDistanceInKm

data class ComprehensiveRunRecordResponse(
    @SerializedName("averagePace")
    val averagePace: Double?,
    @SerializedName("totalDistance")
    val totalDistance: Double?,
    @SerializedName("totalRunningTime")
    val totalRunningTime: TotalRunningTime?
)

fun ComprehensiveRunRecordResponse.toDomainModel() = ComprehensiveRunRecord(
    averagePace = formatPace(this.averagePace ?: 0.0),
    totalDistance = formatDistanceInKm(this.totalDistance?.toInt() ?: 0),
    totalRunningTime = this.totalRunningTime?.toElapsedTimeString() ?: "00:00"
)
