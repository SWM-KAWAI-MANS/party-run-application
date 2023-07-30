package online.partyrun.partyrunapplication.core.network.model.response

import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserInfo

data class GoogleUserInfoResponse(
    val userData: GoogleUserDataResponse?,
    val errorMessage: String?
)

data class GoogleUserDataResponse(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)

fun GoogleUserInfoResponse.toDomainModel() = GoogleUserInfo(
    userData = this.userData?.toDomainModel(),
    errorMessage = this.errorMessage
)


fun GoogleUserDataResponse.toDomainModel() = GoogleUserData(
    userId = this.userId,
    username = this.username,
    profilePictureUrl = this.profilePictureUrl
)
