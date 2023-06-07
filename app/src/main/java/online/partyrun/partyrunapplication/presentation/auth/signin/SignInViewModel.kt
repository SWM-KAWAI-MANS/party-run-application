package online.partyrun.partyrunapplication.presentation.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.data.model.GoogleIdToken
import online.partyrun.partyrunapplication.di.network.TokenManager
import online.partyrun.partyrunapplication.domain.use_case.SignInUseCase
import online.partyrun.partyrunapplication.network.ApiResponse
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val tokenManager: TokenManager
): ViewModel() {

    private val _signInGoogleState = MutableStateFlow(SignInGoogleState())
    val signInGoogleState: StateFlow<SignInGoogleState> = _signInGoogleState.asStateFlow()

    fun onSignInGoogleResult(result: SignInGoogleResult) {
        _signInGoogleState.update {
            it.copy(
                isSignInSuccessful = result.userData != null,
                signInError = result.errorMessage,
            )
        }
    }
    fun signInGoogleLoadingIndicator() {
        _signInGoogleState.update {
            it.copy(
                onSignInIndicator = true
            )
        }
    }
    fun signInGoogleTokenToServer(idToken: GoogleIdToken) = viewModelScope.launch(Dispatchers.IO) {
        signInUseCase.signInGoogleTokenToServer(idToken).collect() {
            when(it) {
                is ApiResponse.Success -> {
                    _signInGoogleState.update { state ->
                        state.copy(
                            sendIdTokenToServer = true,
                            onSignInIndicator = false
                        )
                    }
                    tokenManager.saveAccessToken(it.data.accessToken)
                    tokenManager.saveRefreshToken(it.data.refreshToken)
                }
                is ApiResponse.Failure -> {
                    Timber.tag("SignInGoogleTokenToServer").e("${it.code} ${it.errorMessage}")
                }
                ApiResponse.Loading ->  {
                    Timber.tag("SignInGoogleTokenToServer").e("Loading")
                }
            }
        }
    }

    fun resetState() {
        _signInGoogleState.update {
            SignInGoogleState()
        }
    }
}
