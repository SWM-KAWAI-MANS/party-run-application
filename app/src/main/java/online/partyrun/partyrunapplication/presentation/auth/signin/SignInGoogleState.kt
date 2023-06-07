package online.partyrun.partyrunapplication.presentation.auth.signin
data class SignInGoogleState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val sendIdTokenToServer: Boolean = false,
    val onSignInIndicator: Boolean = false
)
