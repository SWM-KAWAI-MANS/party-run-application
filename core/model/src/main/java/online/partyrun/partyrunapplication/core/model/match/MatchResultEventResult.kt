package online.partyrun.partyrunapplication.core.model.match

import com.google.gson.annotations.SerializedName

data class MatchResultEventResult(
    @SerializedName("members")
    val members: List<MatchMember>,
    @SerializedName("status")
    val status: String
)
