package online.partyrun.partyrunapplication.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.domain.auth.GoogleSignOutUseCase
import online.partyrun.partyrunapplication.core.domain.member.DeleteAccountUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val googleSignOutUseCase: GoogleSignOutUseCase

) : ViewModel() {
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState

    fun deleteAccount() = viewModelScope.launch {
        deleteAccountUseCase().collect {
            when (it) {
                is ApiResponse.Success -> {
                    _settingsUiState.update { state ->
                        state.copy(
                            isAccountDeletionSuccess = true,
                        )
                    }
                    googleSignOutUseCase()
                }

                is ApiResponse.Failure -> {
                    Timber.e("$it")
                }

                ApiResponse.Loading -> {
                    Timber.d("$it")
                }
            }
        }
    }

}