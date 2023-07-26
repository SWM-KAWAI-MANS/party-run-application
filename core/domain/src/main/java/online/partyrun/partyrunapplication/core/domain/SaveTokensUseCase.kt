package online.partyrun.partyrunapplication.core.domain

import online.partyrun.partyrunapplication.core.data.repository.TokenRepository
import javax.inject.Inject

class SaveTokensUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String) {
        tokenRepository.saveAccessToken(accessToken)
        tokenRepository.saveRefreshToken(refreshToken)
    }

}