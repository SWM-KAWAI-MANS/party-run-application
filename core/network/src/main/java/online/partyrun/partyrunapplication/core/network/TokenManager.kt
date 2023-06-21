package online.partyrun.partyrunapplication.core.network

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.network.di.accessDataStore
import online.partyrun.partyrunapplication.core.network.di.refreshDataStore

class TokenManager(
    private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    /**
     * getAccessToken(): 비동기로 String 타입 Token 방출
     * DataStore에서 토큰을 가져오는 동작 수행
     */
    fun getAccessToken(): Flow<String?> {
        return context.accessDataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] // preferences 객체에서 TOKEN_KEY에 해당하는 값 반환
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return context.refreshDataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY] // preferences 객체에서 TOKEN_KEY에 해당하는 값 반환
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        context.accessDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    suspend fun saveRefreshToken(refreshToken: String?) {
        context.refreshDataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken?:"null"
        }
    }

    suspend fun deleteAccessToken() {
        context.accessDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }

    /*
    suspend fun deleteRefreshToken() {
        context.refreshDataStore.edit { preferences ->
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
    */

}
