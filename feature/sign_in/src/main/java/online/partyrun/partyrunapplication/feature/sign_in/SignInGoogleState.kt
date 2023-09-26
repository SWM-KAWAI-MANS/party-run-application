package online.partyrun.partyrunapplication.feature.sign_in
data class SignInGoogleState(
    val isSignInSuccessful: Boolean = false,
    val hasSignInError: String? = null,
    val isIdTokenSentToServer: Boolean = false,
    val isSignInIndicatorOn: Boolean = false,
    val isUserDataSaved: Boolean = false,
    val isSignInBtnEnabled: Boolean = true
)
