package online.partyrun.partyrunapplication.feature.running_result.single

import online.partyrun.partyrunapplication.core.model.running_result.ui.SingleResultUiModel

sealed class SingleResultUiState {
    object Loading : SingleResultUiState()

    data class Success(
        val singleResult: SingleResultUiModel = SingleResultUiModel()
    ) : SingleResultUiState()

    object LoadFailed : SingleResultUiState()
}
