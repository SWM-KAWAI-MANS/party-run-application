package online.partyrun.partyrunapplication.data.model

import com.google.gson.annotations.SerializedName

data class SignInTokenResponse(
    /* TODO : Name 변경 요 */
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String = ""
)
