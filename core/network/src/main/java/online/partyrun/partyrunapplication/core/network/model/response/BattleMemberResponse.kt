package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.match.RunnerInfo

data class BattleMemberResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile")
    val profile: String
)

fun BattleMemberResponse.toDomainModel() = RunnerInfo(
    id = this.id,
    name = this.name,
    profile = this.profile
)
