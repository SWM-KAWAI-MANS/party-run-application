package online.partyrun.partyrunapplication.feature.signin

data class SignInGoogleResult(
    val userData: UserGoogleData?,
    val errorMessage: String?
)

data class UserGoogleData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)
