package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.datastore.datasource.TokenDataSource
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {
    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        tokenDataSource.saveAccessToken(accessToken)
        tokenDataSource.saveRefreshToken(refreshToken)
    }
    override fun getAccessToken(): Flow<String?> {
        return tokenDataSource.getAccessToken()
    }
    override fun getRefreshToken(): Flow<String?> {
        return tokenDataSource.getRefreshToken()
    }
    override suspend fun deleteAccessToken() {
        tokenDataSource.deleteAccessToken()
    }
}
