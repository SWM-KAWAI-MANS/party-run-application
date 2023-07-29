package online.partyrun.partyrunapplication.core.network.model.response

import online.partyrun.partyrunapplication.core.model.signin.GoogleUserData
import online.partyrun.partyrunapplication.core.model.signin.GoogleUserInfo

data class GoogleUserInfoResponse(
    val userData: GoogleUserDataResponse?,
    val errorMessage: String?
)

data class GoogleUserDataResponse(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)

fun GoogleUserInfoResponse.toDomainModel() = this.userData?.let {
    GoogleUserInfo(
        userData = it.toDomainModel(),
        errorMessage = this.errorMessage ?: ""
    )
}

fun GoogleUserDataResponse.toDomainModel() = GoogleUserData(
    userId = this.userId,
    username = this.username ?: "",
    profilePictureUrl = this.profilePictureUrl ?: ""
)
