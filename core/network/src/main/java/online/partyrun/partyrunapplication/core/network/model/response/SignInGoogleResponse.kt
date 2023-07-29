package online.partyrun.partyrunapplication.core.network.model.response

data class SignInGoogleResponse(
    val userData: UserGoogleData?,
    val errorMessage: String?
)

data class UserGoogleData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)