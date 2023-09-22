package online.partyrun.partyrunapplication.core.model.party

data class PartyEvent(
    val entryCode: String = "",
    val distance: Int = 0,
    val managerId: String = "",
    val status: String = "",
    val participants: List<String> = emptyList(),
    val battleId: String? = null
)
