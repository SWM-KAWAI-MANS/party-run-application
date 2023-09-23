package online.partyrun.partyrunapplication.feature.party.room

import online.partyrun.partyrunapplication.core.model.match.RunnerInfo

data class PartyRoomState(
    val entryCode: String = "",
    val distance: Int = 0,
    val status: String = "",
    val battleId: String? = null,
    val manager: RunnerInfo? = null,
    val participants: List<RunnerInfo> = emptyList()
)

