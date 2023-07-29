package online.partyrun.partyrunapplication.core.model.match

data class MatchResultEvent(
    val members: List<MatchMember>,
    val status: String
)
