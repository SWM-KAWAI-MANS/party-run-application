package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.my_page.TotalRunningTime

data class TotalRunningTimeResponse(
    @SerializedName("hours")
    val hours: Int?,
    @SerializedName("minutes")
    val minutes: Int?,
    @SerializedName("seconds")
    val seconds: Int?
)

fun TotalRunningTimeResponse.toDomainModel() = TotalRunningTime(
    hours = this.hours ?: 0,
    minutes = this.minutes ?: 0,
    seconds = this.seconds ?: 0
)
