package online.partyrun.partyrunapplication.core.domain.auth

import online.partyrun.partyrunapplication.core.data.repository.TokenRepository
import javax.inject.Inject

class SaveTokensUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String) {
        tokenRepository.saveTokens(accessToken, refreshToken)
    }
}
