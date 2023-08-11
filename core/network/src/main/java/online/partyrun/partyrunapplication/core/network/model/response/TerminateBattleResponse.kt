package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.battle.TerminateBattle

data class TerminateBattleResponse(
    @SerializedName("message")
    val message: String?
)

fun TerminateBattleResponse.toDomainModel() = TerminateBattle(
    message = this.message ?: ""
)

