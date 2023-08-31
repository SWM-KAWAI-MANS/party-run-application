package online.partyrun.partyrunapplication.feature.splash.agreement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.agreement.SaveAgreementStateUseCase
import javax.inject.Inject

@HiltViewModel
class AgreementViewModel @Inject constructor(
    private val saveAgreementUseCase: SaveAgreementStateUseCase
) : ViewModel() {

    private val _agreementUiState = MutableStateFlow(AgreementUiState())
    val agreementUiState: StateFlow<AgreementUiState> = _agreementUiState.asStateFlow()

    fun saveAgreementState(isChecked: Boolean) = viewModelScope.launch {
        saveAgreementUseCase(isChecked)
    }

    fun onCheckedChangeAllAgreement() {
        val newStateValue =
            !(_agreementUiState.value.isTermsOfServiceChecked && _agreementUiState.value.isPrivacyPolicyChecked)
        _agreementUiState.update { state ->
            state.copy(
                isTermsOfServiceChecked = newStateValue,
                isPrivacyPolicyChecked = newStateValue
            )
        }
        updateAllConditions()
    }

    fun setTermsOfServiceChecked() {
        toggleAndNotify { it.copy(isTermsOfServiceChecked = !it.isTermsOfServiceChecked) }
    }

    fun setPrivacyPolicyChecked() {
        toggleAndNotify { it.copy(isPrivacyPolicyChecked = !it.isPrivacyPolicyChecked) }
    }

    private fun toggleAndNotify(updater: (AgreementUiState) -> AgreementUiState) {
        _agreementUiState.update(updater)
        updateAllConditions()
    }

    private fun updateAllConditions() {
        val isAllChecked =
            _agreementUiState.value.isTermsOfServiceChecked && _agreementUiState.value.isPrivacyPolicyChecked
        _agreementUiState.update { state ->
            state.copy(isAllChecked = isAllChecked)
        }
    }
}
