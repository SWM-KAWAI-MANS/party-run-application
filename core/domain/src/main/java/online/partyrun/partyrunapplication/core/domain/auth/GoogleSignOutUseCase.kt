package online.partyrun.partyrunapplication.core.domain.auth

import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import javax.inject.Inject

class GoogleSignOutUseCase @Inject constructor(
    private val signInRepository: SignInRepository
) {
    suspend operator fun invoke() {
        signInRepository.signOutGoogleAuth()
    }

}
