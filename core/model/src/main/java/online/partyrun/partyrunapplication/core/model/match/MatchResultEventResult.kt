package online.partyrun.partyrunapplication.core.model.match

import com.google.gson.annotations.SerializedName

data class MatchResultEventResult(
    @SerializedName("status")
    val status: String = "WAIT",
    @SerializedName("location")
    val location: String? = null
)
