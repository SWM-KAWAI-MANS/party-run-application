package online.partyrun.partyrunapplication.feature.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.model.GoogleIdToken
import online.partyrun.partyrunapplication.core.network.TokenManager
import online.partyrun.partyrunapplication.core.domain.SignInUseCase
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.SignInGoogleResult
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
    fun signInGoogleTokenToServer(idToken: GoogleIdToken) = viewModelScope.launch() {
        signInUseCase(idToken).collect() {
            when(it) {
                is ApiResponse.Success -> {
                    _signInGoogleState.update { state ->
                        state.copy(
                            sendIdTokenToServer = true,
                            onSignInIndicator = false
                        )
                    }
                    Timber.tag("TEST ACCESS TOKEN").e(it.data.accessToken)
                    Timber.tag("TEST REFRESH TOKEN").e(it.data.refreshToken)
                    tokenManager.saveAccessToken(it.data.accessToken)
                    tokenManager.saveRefreshToken(it.data.refreshToken)
                }
                is ApiResponse.Failure -> {
                    _signInGoogleState.update { state ->
                        state.copy(
                            isSignInSuccessful = false
                        )
                    }
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
