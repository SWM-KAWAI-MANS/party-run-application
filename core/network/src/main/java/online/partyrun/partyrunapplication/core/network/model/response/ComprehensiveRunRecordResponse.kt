package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.my_page.ComprehensiveRunRecord
import online.partyrun.partyrunapplication.core.model.my_page.TotalRunningTime

data class ComprehensiveRunRecordResponse(
    @SerializedName("averagePace")
    val averagePace: Double?,
    @SerializedName("totalDistance")
    val totalDistance: Double?,
    @SerializedName("totalRunningTime")
    val totalRunningTime: TotalRunningTime?
)

fun ComprehensiveRunRecordResponse.toDomainModel() = ComprehensiveRunRecord(
    averagePace = this.averagePace ?: 0.0,
    totalDistance = this.totalDistance ?: 0.0,
    totalRunningTime = this.totalRunningTime ?: TotalRunningTime(0, 0, 0)
)
