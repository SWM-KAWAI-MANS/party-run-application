package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.match.MatchMember

data class MatchResultEventResponse(
    @SerializedName("members")
    val members: List<MatchMember>,
    @SerializedName("status")
    val status: String
)
