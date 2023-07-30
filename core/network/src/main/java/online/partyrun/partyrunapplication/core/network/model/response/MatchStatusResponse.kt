package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.match.MatchStatus

data class MatchStatusResponse(
    @SerializedName("message")
    val message: String?
)

fun MatchStatusResponse.toDomainModel() = MatchStatus(
    message = this.message ?: ""
)
