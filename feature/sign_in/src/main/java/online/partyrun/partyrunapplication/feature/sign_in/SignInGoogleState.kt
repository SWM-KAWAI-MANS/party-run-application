package online.partyrun.partyrunapplication.feature.sign_in
data class SignInGoogleState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val sendIdTokenToServer: Boolean = false,
    val onSignInIndicator: Boolean = false
)
