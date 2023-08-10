package online.partyrun.partyrunapplication.core.network.model.response


import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.battle.BattleId

data class BattleIdResponse(
    @SerializedName("id")
    val id: String
)

fun BattleIdResponse.toDomainModel() = BattleId(
    id = this.id
)