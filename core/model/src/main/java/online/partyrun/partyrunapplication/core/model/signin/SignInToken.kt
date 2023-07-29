package online.partyrun.partyrunapplication.core.model.signin

data class SignInToken(
    val accessToken: String? = "",
    val refreshToken: String? = ""
)
