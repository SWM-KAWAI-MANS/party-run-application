package online.partyrun.partyrunapplication.core.network.model.request

import online.partyrun.partyrunapplication.core.model.running.GpsData
import online.partyrun.partyrunapplication.core.model.running.RecordData

data class RecordDataRequest(
    val record: List<GpsData>
)

fun RecordData.toRequestModel() : RecordDataRequest {
    return RecordDataRequest(
        record = this.record
    )
}
