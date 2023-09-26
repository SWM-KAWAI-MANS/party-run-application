package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.common.Constants.S3_URL
import online.partyrun.partyrunapplication.core.model.user.User

data class UserResponse(
    @SerializedName("id")
    val userId: String?,
    @SerializedName("name")
    val nickName: String?,
    @SerializedName("profile")
    val profileImage: String?,
)

fun UserResponse.toDomainModel() = User(
    id = this.userId ?: "",
    nickName = this.nickName ?: "",
    profileImage = S3_URL.plus("/" + this.profileImage)
)
