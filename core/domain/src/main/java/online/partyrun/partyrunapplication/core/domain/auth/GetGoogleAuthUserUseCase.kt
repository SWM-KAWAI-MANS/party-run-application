package online.partyrun.partyrunapplication.core.domain.auth

import online.partyrun.partyrunapplication.core.data.repository.GoogleAuthRepository
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import javax.inject.Inject

class GetGoogleAuthUserUseCase @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) {
    operator fun invoke(): GoogleUserData? {
        return googleAuthRepository.getGoogleAuthUser()
    }
}