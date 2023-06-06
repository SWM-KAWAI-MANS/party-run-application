package online.partyrun.partyrunapplication.presentation.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(

): ViewModel() {

    private val _signInGoogleState = MutableStateFlow(SignInGoogleState())
    val signInGoogleState = _signInGoogleState.asStateFlow()

    fun onSignInGoogleResult(result: SignInGoogleResult) {
        _signInGoogleState.update {
            it.copy(
                isSignInSuccessful = result.userData != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _signInGoogleState.update {
            SignInGoogleState()
        }
    }
}
