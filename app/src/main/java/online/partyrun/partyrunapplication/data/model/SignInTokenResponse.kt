package online.partyrun.partyrunapplication.data.model

import com.google.gson.annotations.SerializedName

data class SignInTokenResponse(
    /* TODO : Name 변경 요 */
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)
