package online.partyrun.partyrunapplication.feature.party.room

import online.partyrun.partyrunapplication.core.model.party.PartyEvent

sealed class PartyRoomUiState {
    object Loading : PartyRoomUiState()

    data class Success(
        val partyRoomState: PartyRoomState = PartyRoomState()
    ) : PartyRoomUiState()

    object LoadFailed : PartyRoomUiState()
}

fun PartyRoomUiState.updateState(
    newEvent: PartyEvent? = null,
): PartyRoomUiState {
    return when (this) {
        is PartyRoomUiState.Success -> this.copy(
            partyRoomState = this.partyRoomState.copy(
                partyEvent = newEvent ?: this.partyRoomState.partyEvent
            )
        )

        else -> this
    }
}
