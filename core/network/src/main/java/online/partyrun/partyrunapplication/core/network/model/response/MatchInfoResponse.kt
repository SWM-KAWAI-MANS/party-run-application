package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.match.RunnerIds

data class MatchInfoResponse(
    @SerializedName("members")
    val members: List<MatchMemberResponse>
)

fun MatchInfoResponse.toDomainModel(): RunnerIds {
    return RunnerIds(runnerIds = this.members.map { it.id })
}
