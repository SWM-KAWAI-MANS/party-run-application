package online.partyrun.partyrunapplication.core.network.model.response

import com.google.gson.annotations.SerializedName
import online.partyrun.partyrunapplication.core.model.signin.SignInToken

/**
 * Network representation of [SignInToken]
 */
data class SignInTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?
)

fun SignInTokenResponse.toDomainModel() = SignInToken(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)
