package online.partyrun.partyrunapplication.core.model.match

import com.google.gson.annotations.SerializedName

data class MatchResultEventResponse(
    @SerializedName("status")
    val status: String = "WAIT",
    @SerializedName("location")
    val location: String? = null
)
