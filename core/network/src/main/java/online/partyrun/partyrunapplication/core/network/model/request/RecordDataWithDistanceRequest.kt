package online.partyrun.partyrunapplication.core.network.model.request

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running.GpsDataWithDistance
import online.partyrun.partyrunapplication.core.model.running.RecordDataWithDistance
import online.partyrun.partyrunapplication.core.model.running.RunningTime

data class RecordDataWithDistanceRequest(
    @SerializedName("records")
    val records: List<GpsDataWithDistance>,
    @SerializedName("runningTime")
    val runningTime: RunningTime
)

fun RecordDataWithDistance.toRequestModel(runningTime: RunningTime): RecordDataWithDistanceRequest {
    return RecordDataWithDistanceRequest(
        records = this.records,
        runningTime = runningTime
    )
}
