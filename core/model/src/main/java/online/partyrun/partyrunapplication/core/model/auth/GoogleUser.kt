package online.partyrun.partyrunapplication.core.model.auth

data class GoogleUserInfo(
    val userData: GoogleUserData,
    val errorMessage: String
)

data class GoogleUserData(
    val userId: String,
    val username: String,
    val profilePictureUrl: String
)
