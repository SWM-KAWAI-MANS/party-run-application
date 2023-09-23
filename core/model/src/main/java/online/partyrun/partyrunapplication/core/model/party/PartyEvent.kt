package online.partyrun.partyrunapplication.core.model.party

enum class PartyEventStatus(val status: String) {
    WAITING("WAITING"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED")
}

data class PartyEvent(
    val entryCode: String = "",
    val distance: Int = 0,
    val managerId: String = "",
    val status: PartyEventStatus = PartyEventStatus.WAITING,
    val participants: List<String> = emptyList(),
    val battleId: String? = null
)
