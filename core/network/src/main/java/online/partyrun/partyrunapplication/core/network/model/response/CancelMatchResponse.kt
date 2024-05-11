package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.match.CancelMatch

data class CancelMatchResponse(
    @SerializedName("message")
    val message: String?
)

fun CancelMatchResponse.toDomainModel() = CancelMatch(
    message = this.message.orEmpty()
)
