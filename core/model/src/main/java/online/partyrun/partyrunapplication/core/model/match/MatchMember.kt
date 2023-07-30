package online.partyrun.partyrunapplication.core.model.match

enum class PlayerStatus {
    NO_RESPONSE,
    READY,
    CANCELED,
}

data class MatchMember(
    val id: String,
    val status: String
)
