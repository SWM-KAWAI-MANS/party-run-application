package online.partyrun.partyrunapplication.core.model.running

import com.google.gson.annotations.SerializedName


data class RecordData(
    @SerializedName("record")
    val record: List<GpsData>
)
