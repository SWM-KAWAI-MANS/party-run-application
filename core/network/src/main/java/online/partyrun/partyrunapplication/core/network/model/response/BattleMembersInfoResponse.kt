package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData

data class BattleMembersInfoResponse(
    @SerializedName("members")
    val members: List<BattleMemberResponse>
)

fun BattleMembersInfoResponse.toDomainModel() = RunnerInfoData(
    runners = this.members.map { it.toDomainModel() }
)
