package online.partyrun.partyrunapplication.feature.splash.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.auth.GetGoogleAuthUserUseCase
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getGoogleAuthUserUseCase: GetGoogleAuthUserUseCase
): ViewModel() {

    private val _googleUser = MutableStateFlow<GoogleUserData?>(null)
    val googleUser: StateFlow<GoogleUserData?> = _googleUser.asStateFlow()

    init {
        getGoogleAuthUser()
    }
    private fun getGoogleAuthUser() = viewModelScope.launch {
        getGoogleAuthUserUseCase().collect {
            _googleUser.value = it
        }
    }

}
