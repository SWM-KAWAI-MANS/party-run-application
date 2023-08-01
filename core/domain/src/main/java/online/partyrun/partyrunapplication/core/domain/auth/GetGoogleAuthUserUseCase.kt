package online.partyrun.partyrunapplication.core.domain.auth

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.data.repository.GoogleAuthRepository
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import javax.inject.Inject

class GetGoogleAuthUserUseCase @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) {
    operator fun invoke(): Flow<GoogleUserData?> {
        return googleAuthRepository.getGoogleAuthUser()
    }
}