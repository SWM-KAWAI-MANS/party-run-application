package online.partyrun.partyrunapplication.core.network.model.request

import online.partyrun.partyrunapplication.core.model.running.GpsDataWithDistance
import online.partyrun.partyrunapplication.core.model.running.RecordDataWithDistance
import online.partyrun.partyrunapplication.core.model.running.RunningTime

data class RecordDataWithDistanceRequest(
    val runningTime: RunningTime,
    val records: List<GpsDataWithDistance>
)

fun RecordDataWithDistance.toRequestModel(runningTime: RunningTime): RecordDataWithDistanceRequest {
    return RecordDataWithDistanceRequest(
        records = this.records,
        runningTime = runningTime
    )
}
