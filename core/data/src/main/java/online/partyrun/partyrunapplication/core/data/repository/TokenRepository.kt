package online.partyrun.partyrunapplication.core.data.repository

interface TokenRepository {
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
}
