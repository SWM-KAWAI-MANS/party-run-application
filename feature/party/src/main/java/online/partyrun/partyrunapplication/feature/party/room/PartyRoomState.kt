package online.partyrun.partyrunapplication.feature.party.room

import online.partyrun.partyrunapplication.core.model.party.PartyEvent

data class PartyRoomState(
    val partyEvent: PartyEvent = PartyEvent()
)
