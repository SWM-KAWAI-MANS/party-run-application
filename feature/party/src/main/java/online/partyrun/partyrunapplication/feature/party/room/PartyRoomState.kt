package online.partyrun.partyrunapplication.feature.party.room

import online.partyrun.partyrunapplication.core.model.match.RunnerInfo
import online.partyrun.partyrunapplication.core.model.party.PartyEventStatus

data class PartyRoomState(
    val entryCode: String = "",
    val distance: Int = 0,
    val status: PartyEventStatus = PartyEventStatus.WAITING,
    val battleId: String? = null,
    val manager: RunnerInfo? = null,
    val participants: List<RunnerInfo> = emptyList()
)

