package online.partyrun.partyrunapplication.feature.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.domain.auth.GetSignInTokenUseCase
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.domain.auth.SaveTokensUseCase
import online.partyrun.partyrunapplication.core.network.model.response.GoogleUserInfoResponse
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getSignInTokenUseCase: GetSignInTokenUseCase,
    private val saveTokensUseCase: SaveTokensUseCase
): ViewModel() {

    private val _signInGoogleState = MutableStateFlow(SignInGoogleState())
    val signInGoogleState: StateFlow<SignInGoogleState> = _signInGoogleState.asStateFlow()

    fun onSignInGoogleResult(result: GoogleUserInfoResponse) {
        _signInGoogleState.update {
            it.copy(
                isSignInSuccessful = result.userData != null,
                hasSignInError = result.errorMessage,
            )
        }
    }
    fun signInGoogleLoadingIndicator() {
        _signInGoogleState.update {
            it.copy(
                isSignInIndicatorOn = true
            )
        }
    }
    fun signInWithGoogleTokenViaServer(idToken: GoogleIdToken) = viewModelScope.launch {
        getSignInTokenUseCase(idToken).collect() {
            when(it) {
                is ApiResponse.Success -> {
                    _signInGoogleState.update { state ->
                        state.copy(
                            isIdTokenSentToServer = true,
                            isSignInIndicatorOn = false
                        )
                    }
                    saveTokensUseCase(
                        accessToken = it.data.accessToken ?: "",
                        refreshToken = it.data.refreshToken ?: ""
                    )
                }
                is ApiResponse.Failure -> {
                    _signInGoogleState.update { state ->
                        state.copy(
                            isSignInSuccessful = false
                        )
                    }
                    Timber.tag("SignInViewModel").e("${it.code} ${it.errorMessage}")
                }
                ApiResponse.Loading ->  {
                    Timber.tag("SignInViewModel").d("Loading")
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
