package online.partyrun.partyrunapplication.feature.splash.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.agreement.GetAgreementStateUseCase
import online.partyrun.partyrunapplication.core.domain.auth.GetGoogleAuthUserUseCase
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAgreementStateUseCase: GetAgreementStateUseCase,
    private val getGoogleAuthUserUseCase: GetGoogleAuthUserUseCase
): ViewModel() {

    private val _isAgreement = MutableStateFlow(false)
    val isAgreement: StateFlow<Boolean> = _isAgreement.asStateFlow()

    private val _googleUser = MutableStateFlow<GoogleUserData?>(null)
    val googleUser: StateFlow<GoogleUserData?> = _googleUser.asStateFlow()

    init {
        // getAgreementState()
        getGoogleAuthUser()
    }
    private fun getGoogleAuthUser() = viewModelScope.launch {
        _googleUser.value = getGoogleAuthUserUseCase()
    }

    private fun getAgreementState() = viewModelScope.launch {
        getAgreementStateUseCase().collect { checked ->
            _isAgreement.value = checked
        }
    }
}
