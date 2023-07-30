package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.match.WaitingStatus

data class WaitingEventResponse(
    @SerializedName("status")
    val status: WaitingStatus = WaitingStatus.CONNECT,
    @SerializedName("message")
    val message: String?
)
