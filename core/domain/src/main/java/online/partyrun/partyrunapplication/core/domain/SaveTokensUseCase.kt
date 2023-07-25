package online.partyrun.partyrunapplication.core.domain

import online.partyrun.partyrunapplication.core.data.repository.TokenRepository
import javax.inject.Inject

class SaveTokensUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun saveAccessToken(accessToken: String) {
        tokenRepository.saveAccessToken(accessToken)
    }
    suspend fun saveRefreshToken(refreshToken: String) {
        tokenRepository.saveRefreshToken(refreshToken)
    }

}