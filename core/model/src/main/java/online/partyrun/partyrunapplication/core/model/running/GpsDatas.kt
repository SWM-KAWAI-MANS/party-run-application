package online.partyrun.partyrunapplication.core.model.running

import com.google.gson.annotations.SerializedName


data class GpsDatas(
    @SerializedName("gpsDatas")
    val gpsDatas: List<GpsData>
)