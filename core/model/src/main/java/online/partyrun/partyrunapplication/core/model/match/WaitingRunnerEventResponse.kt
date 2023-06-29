package online.partyrun.partyrunapplication.core.model.match

import com.google.gson.annotations.SerializedName

data class WaitingRunnerEventResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean = false,
    @SerializedName("message")
    val message: String = ""
)
