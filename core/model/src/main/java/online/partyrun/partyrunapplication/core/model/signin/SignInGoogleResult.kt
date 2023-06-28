package online.partyrun.partyrunapplication.core.model.signin

data class SignInGoogleResult(
    val userData: UserGoogleData?,
    val errorMessage: String?
)

data class UserGoogleData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)