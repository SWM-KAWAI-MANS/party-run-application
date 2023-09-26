package online.partyrun.partyrunapplication.core.network.model.request

import online.partyrun.partyrunapplication.core.model.match.MatchDecision

data class MatchDecisionRequest(
    val isJoin: Boolean
)

fun MatchDecision.toRequestModel() : MatchDecisionRequest {
    return MatchDecisionRequest(
        isJoin = this.isJoin
    )
}
