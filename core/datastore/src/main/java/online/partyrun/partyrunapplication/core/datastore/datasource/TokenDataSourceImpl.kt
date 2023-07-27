package online.partyrun.partyrunapplication.core.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class TokenDataSourceImpl @Inject constructor(
    private val tokenDataStore: DataStore<Preferences>
) : TokenDataSource {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    /**
     * getAccessToken(): 비동기로 String 타입 Token 방출
     * DataStore에서 토큰을 가져오는 동작 수행
     */
    override fun getAccessToken(): Flow<String?> {
        return tokenDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Timber.tag("TokenDataSource").e(exception, "Failed to get access token")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[ACCESS_TOKEN_KEY]
            }
    }

    override fun getRefreshToken(): Flow<String?> {
        return tokenDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Timber.tag("TokenDataSource").e(exception, "Failed to get refresh token")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[REFRESH_TOKEN_KEY]
            }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        tokenDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        tokenDataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun deleteAccessToken() {
        tokenDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }

}
