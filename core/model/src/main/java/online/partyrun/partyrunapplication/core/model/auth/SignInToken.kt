package online.partyrun.partyrunapplication.core.model.auth

data class SignInToken(
    val accessToken: String? = "",
    val refreshToken: String? = ""
)
