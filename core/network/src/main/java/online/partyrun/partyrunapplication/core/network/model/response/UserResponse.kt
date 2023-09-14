package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.common.Constants.S3_URL
import online.partyrun.partyrunapplication.core.model.user.User

data class UserResponse(
    @SerializedName("id")
    val userId: String?,
    @SerializedName("name")
    val userName: String?,
    @SerializedName("profile")
    val userProfile: String?,
)

fun UserResponse.toDomainModel() = User(
    id = this.userId ?: "",
    name = this.userName ?: "",
    profile = S3_URL.plus(this.userProfile)
)
