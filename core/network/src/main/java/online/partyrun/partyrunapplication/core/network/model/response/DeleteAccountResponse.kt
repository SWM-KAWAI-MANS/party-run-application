package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.user.DeleteAccount

data class DeleteAccountResponse(
    @SerializedName("message")
    val message: String?
)

fun DeleteAccountResponse.toDomainModel() = DeleteAccount(
    message = this.message ?: ""
)
