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
import online.partyrun.partyrunapplication.core.domain.my_page.DeleteRunningHistoryUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val googleSignOutUseCase: GoogleSignOutUseCase,
    private val deleteRunningHistoryUseCase: DeleteRunningHistoryUseCase,
    private val saveAgreementStateUseCase: SaveAgreementStateUseCase
) : ViewModel() {
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun deleteAccountProcess() {
        signOutFromGoogle()
        saveAgreementState(isChecked = false)
        deleteAccount()
        deleteAllHistories()
    }

    fun signOut() {
        signOutFromGoogle()
        deleteAllHistories()
        saveAgreementState(isChecked = false)
    }

    private fun deleteAccount() = viewModelScope.launch {
        deleteAccountUseCase().collect { result ->
            result.onSuccess {
                _settingsUiState.update { state ->
                    state.copy(
                        isAccountDeletionSuccess = true
                    )
                }
            }.onFailure { errorMessage, code ->
                Timber.e("$code $errorMessage")
            }
        }
    }

    private fun deleteAllHistories() = viewModelScope.launch {
        deleteRunningHistoryUseCase().collect { result ->
            result.onSuccess {
                Timber.d("모든 러닝 기록 삭제 성공")
            }.onFailure { errorMessage, code ->
                _snackbarMessage.value = "러닝 기록 삭제 실패"
                Timber.e("$code $errorMessage")
            }
        }
    }

    private fun signOutFromGoogle() = viewModelScope.launch {
        googleSignOutUseCase()
    }

    private fun saveAgreementState(isChecked: Boolean) = viewModelScope.launch {
        saveAgreementStateUseCase(isChecked)
    }

}
