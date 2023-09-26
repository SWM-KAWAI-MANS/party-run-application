package online.partyrun.partyrunapplication.core.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun deleteAccessToken()
}
