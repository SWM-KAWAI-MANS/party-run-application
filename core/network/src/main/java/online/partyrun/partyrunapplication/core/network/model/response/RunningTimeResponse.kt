package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.running.RunningTime

data class RunningTimeResponse(
    @SerializedName("hours")
    val hours: Int,
    @SerializedName("minutes")
    val minutes: Int,
    @SerializedName("seconds")
    val seconds: Int
)

fun RunningTimeResponse.toDomainModel() = RunningTime(
    hours = this.hours,
    minutes = this.minutes,
    seconds = this.seconds
)
