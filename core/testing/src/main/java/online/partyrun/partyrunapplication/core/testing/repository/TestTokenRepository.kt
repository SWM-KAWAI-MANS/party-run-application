package online.partyrun.partyrunapplication.core.testing.repository

import kotlinx.coroutines.flow.MutableStateFlow
import online.partyrun.partyrunapplication.core.data.repository.TokenRepository

/**
 * 각 토큰을 저장하기 위해 MutableStateFlow를 사용하며,
 * 토큰 저장 메서드가 호출될 때마다 해당 MutableStateFlow가 업데이트되도록 한다.
 * getAccessToken() 또는 getRefreshToken()를 호출하여 저장된 토큰 검사.
 */
class TestTokenRepository: TokenRepository {

    private val accessTokenFlow = MutableStateFlow<String?>(null)
    private val refreshTokenFlow = MutableStateFlow<String?>(null)

    fun getAccessToken() = accessTokenFlow.value
    fun getRefreshToken() = refreshTokenFlow.value

    override suspend fun saveAccessToken(accessToken: String) {
        // Update the state when saveAccessToken is called
        accessTokenFlow.emit(accessToken)
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        // Update the state when saveRefreshToken is called
        refreshTokenFlow.emit(refreshToken)
    }
}
