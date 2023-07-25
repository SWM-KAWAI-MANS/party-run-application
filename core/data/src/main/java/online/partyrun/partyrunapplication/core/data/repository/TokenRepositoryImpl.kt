package online.partyrun.partyrunapplication.core.data.repository

import online.partyrun.partyrunapplication.core.datastore.di.TokenDataSource
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {
    override suspend fun saveAccessToken(accessToken: String) {
        tokenDataSource.saveAccessToken(accessToken)
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        tokenDataSource.saveRefreshToken(refreshToken)
    }

}