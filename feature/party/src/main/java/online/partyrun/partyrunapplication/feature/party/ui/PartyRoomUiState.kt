package online.partyrun.partyrunapplication.feature.party.ui

import online.partyrun.partyrunapplication.core.model.party.PartyEvent

data class PartyRoomUiState(
    val partyEvent: PartyEvent = PartyEvent()
)
