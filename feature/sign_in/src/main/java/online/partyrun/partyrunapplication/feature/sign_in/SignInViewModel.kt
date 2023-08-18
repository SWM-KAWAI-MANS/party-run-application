package online.partyrun.partyrunapplication.feature.sign_in

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.domain.auth.GetSignInTokenUseCase
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.domain.auth.GoogleSignInUseCase
import online.partyrun.partyrunapplication.core.domain.auth.GoogleSignOutUseCase
import online.partyrun.partyrunapplication.core.domain.auth.SaveTokensUseCase
import online.partyrun.partyrunapplication.core.domain.member.GetUserDataUseCase
import online.partyrun.partyrunapplication.core.domain.member.SaveUserDataUseCase
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserInfo
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getSignInTokenUseCase: GetSignInTokenUseCase,
    private val googleSignOutUseCase: GoogleSignOutUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase
): ViewModel() {

    private val _signInGoogleState = MutableStateFlow(SignInGoogleState())
    val signInGoogleState: StateFlow<SignInGoogleState> = _signInGoogleState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    private fun onSignInGoogleResult(result: GoogleUserInfo?) {
        _signInGoogleState.update {
            it.copy(
                isSignInSuccessful = result?.userData != null,
                hasSignInError = result?.errorMessage,
            )
        }
    }

    private fun signInGoogleLoadingIndicator() {
        _signInGoogleState.update {
            it.copy(
                isSignInIndicatorOn = true
            )
        }
    }

    fun signInWithGoogle(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) = viewModelScope.launch(Dispatchers.IO) {
        val signInIntentSender = googleSignInUseCase.signInGoogle()
        launcher.launch(
            IntentSenderRequest.Builder(
                signInIntentSender ?: return@launch
            ).build()
        )
    }

    /**
     * handleActivityResult는 ActivityResult의 결과만 처리하며,
     * 이후의 로그인 처리는 signInWithGoogleWithIntent 담당
     */
    fun handleActivityResult(resultCode: Int, data: Intent?) = viewModelScope.launch(Dispatchers.IO) {
        if (resultCode == ComponentActivity.RESULT_OK) {
            signInWithGoogleWithIntent(intent = data)
        }
    }

    private fun signInWithGoogleWithIntent(intent: Intent?) = viewModelScope.launch(Dispatchers.IO) {
        signInGoogleLoadingIndicator()
        val signInResult = intent?.let { googleSignInUseCase.signInGoogleWithIntent(it) }
        if (signInResult != null) {
            onSignInGoogleResult(signInResult)
        }
    }

    fun signInWithGoogleTokenViaServer(idToken: GoogleIdToken) = viewModelScope.launch {
        getSignInTokenUseCase(idToken).collect() {
            when(it) {
                is ApiResponse.Success -> {
                    saveTokensUseCase(
                        accessToken = it.data.accessToken ?: "",
                        refreshToken = it.data.refreshToken ?: ""
                    )
                    _signInGoogleState.update { state ->
                        state.copy(
                            isIdTokenSentToServer = true,
                            isSignInIndicatorOn = false
                        )
                    }
                }
                is ApiResponse.Failure -> {
                    _snackbarMessage.value = "로그인 실패"
                    signOutFromGoogle()
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

    fun saveUserData() {
        viewModelScope.launch {
            getUserDataUseCase().collect {
                when (it) {
                    is ApiResponse.Success -> {
                        saveUserDataUseCase(it.data)
                        _signInGoogleState.update { state ->
                            state.copy(
                                isUserDataSaved = true
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        Timber.e("${it.code} ${it.errorMessage}")
                    }

                    ApiResponse.Loading -> {
                        Timber.d("$it")
                    }
                }
            }
        }
    }

    fun signOutFromGoogle() = viewModelScope.launch {
        googleSignOutUseCase()
    }

    fun resetState() {
        _signInGoogleState.update {
            SignInGoogleState()
        }
    }
}
