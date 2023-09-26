package online.partyrun.partyrunapplication.feature.party.room

sealed class PartyRoomUiState {
    object Loading : PartyRoomUiState()

    data class Success(
        val partyRoomState: PartyRoomState = PartyRoomState()
    ) : PartyRoomUiState()

    object LoadFailed : PartyRoomUiState()
}

fun PartyRoomUiState.updateState(
    partyRoomState: PartyRoomState
): PartyRoomUiState {
    return when (this) {
        is PartyRoomUiState.Success -> {
            this.copy(partyRoomState = partyRoomState)
        }

        else -> this
    }
}
