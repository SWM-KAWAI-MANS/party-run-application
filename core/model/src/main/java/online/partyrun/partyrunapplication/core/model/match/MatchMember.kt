package online.partyrun.partyrunapplication.core.model.match

import com.google.gson.annotations.SerializedName

enum class PlayerStatus {
    NO_RESPONSE,
    READY,
    CANCELED,
}

data class MatchMember(
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: String
)
