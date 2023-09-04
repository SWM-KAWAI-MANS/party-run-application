package online.partyrun.partyrunapplication.feature.single

sealed class SingleUiState {
    object Loading : SingleUiState()
    object Success : SingleUiState()
    object LoadFailed : SingleUiState()
}
