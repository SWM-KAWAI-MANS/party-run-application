package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName

data class MatchMemberResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: String
)