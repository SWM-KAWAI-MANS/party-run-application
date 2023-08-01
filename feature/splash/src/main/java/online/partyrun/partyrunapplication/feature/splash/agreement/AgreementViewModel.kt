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
): ViewModel() {
    private val _agreementUiState = MutableStateFlow(AgreementUiState())
    val agreementUiState: StateFlow<AgreementUiState> = _agreementUiState.asStateFlow()

    fun saveAgreementState(isChecked: Boolean) = viewModelScope.launch {
        saveAgreementUseCase(isChecked)
    }

    fun onCheckedChangeAllAgreement(isChecked: Boolean) {
        _agreementUiState.update { state ->
            state.copy(isAllChecked = isChecked)
        }
    }
}
