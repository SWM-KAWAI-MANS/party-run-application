package online.partyrun.partyrunapplication.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.agreement.SaveAgreementStateUseCase
import online.partyrun.partyrunapplication.core.domain.auth.GoogleSignOutUseCase
import online.partyrun.partyrunapplication.core.domain.member.DeleteAccountUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val googleSignOutUseCase: GoogleSignOutUseCase,
    private val saveAgreementStateUseCase: SaveAgreementStateUseCase
) : ViewModel() {
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun deleteAccount() = viewModelScope.launch {
        deleteAccountUseCase().collect { result ->
            result.onSuccess {
                googleSignOutUseCase()
                _settingsUiState.update { state ->
                    state.copy(
                        isAccountDeletionSuccess = true,
                    )
                }
            }.onFailure { errorMessage, code ->
                Timber.e("$code $errorMessage")
            }
        }
    }

    fun saveAgreementState(isChecked: Boolean) = viewModelScope.launch {
        saveAgreementStateUseCase(isChecked)
    }

}
