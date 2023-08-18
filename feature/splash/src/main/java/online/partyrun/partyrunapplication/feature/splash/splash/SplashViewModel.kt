package online.partyrun.partyrunapplication.feature.splash.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.agreement.GetAgreementStateUseCase
import online.partyrun.partyrunapplication.core.domain.auth.GetGoogleAuthUserUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getGoogleAuthUserUseCase: GetGoogleAuthUserUseCase,
    private val getAgreementStateUseCase: GetAgreementStateUseCase
) : ViewModel() {

    private val _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState: StateFlow<SplashUiState> = _splashUiState.asStateFlow()

    init {
        getAgreementState()
        getGoogleAuthUser()
    }

    private fun getGoogleAuthUser() = viewModelScope.launch {
        getGoogleAuthUserUseCase().collect {
            _splashUiState.update { state ->
                state.copy(
                    googleUser = it
                )
            }
        }
    }

    private fun getAgreementState() = viewModelScope.launch {
        getAgreementStateUseCase().collect {
            _splashUiState.update { state ->
                state.copy(
                    isAgreementChecked = it
                )
            }
        }
    }

}
