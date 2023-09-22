package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.party.PartyCode

data class PartyCodeResponse(
    @SerializedName("code")
    val code: String?
)

fun PartyCodeResponse.toDomainModel() = PartyCode(
    code = this.code ?: ""
)
