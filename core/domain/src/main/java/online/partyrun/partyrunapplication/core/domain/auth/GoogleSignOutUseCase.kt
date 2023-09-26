package online.partyrun.partyrunapplication.core.domain.auth

import online.partyrun.partyrunapplication.core.data.repository.GoogleAuthRepository
import javax.inject.Inject

class GoogleSignOutUseCase @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) {
    suspend operator fun invoke() {
        googleAuthRepository.signOutGoogleAuth()
    }

}
