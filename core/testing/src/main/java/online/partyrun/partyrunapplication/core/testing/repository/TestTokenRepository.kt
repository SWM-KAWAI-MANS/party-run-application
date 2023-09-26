package online.partyrun.partyrunapplication.core.testing.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import online.partyrun.partyrunapplication.core.data.repository.TokenRepository

/**
 * 각 토큰을 저장하기 위해 MutableStateFlow를 사용하며,
 * 토큰 저장 메서드가 호출될 때마다 해당 MutableStateFlow가 업데이트되도록 한다.
 * getAccessToken() 또는 getRefreshToken()를 호출하여 저장된 토큰 검사.
 */
class TestTokenRepository: TokenRepository {

    private var accessToken: String? = null
    private var refreshToken: String? = null

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    /* accessToken과 refreshToken에 대한 Flow를 flowOf를 사용하여 반환 */
    override fun getAccessToken(): Flow<String?> = flowOf(accessToken)

    override fun getRefreshToken(): Flow<String?> = flowOf(refreshToken)

    override suspend fun deleteAccessToken() {
        this.accessToken = null // 단순히 accessToken을 null로 설정하여 구현 -> 토큰을 삭제하는 것과 같은 효과
    }
}
