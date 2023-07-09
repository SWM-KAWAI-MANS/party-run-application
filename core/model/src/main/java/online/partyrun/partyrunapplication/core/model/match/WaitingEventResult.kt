package online.partyrun.partyrunapplication.core.model.match

import com.google.gson.annotations.SerializedName

enum class WaitingMatchStatus {
    CONNECT,
    MATCHED,
    TIMEOUT
}
data class WaitingEventResult(
    @SerializedName("status")
    val status: WaitingMatchStatus = WaitingMatchStatus.CONNECT,
    @SerializedName("message")
    val message: String = ""
)
