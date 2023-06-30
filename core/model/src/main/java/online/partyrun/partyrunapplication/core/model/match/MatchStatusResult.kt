package online.partyrun.partyrunapplication.core.model.match

import com.google.gson.annotations.SerializedName

data class MatchStatusResult(
    @SerializedName("message")
    val message: String
)