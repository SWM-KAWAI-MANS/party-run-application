package online.partyrun.partyrunapplication.core.domain.auth

import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import online.partyrun.partyrunapplication.core.model.auth.SignInToken
import javax.inject.Inject

class GetSignInTokenUseCase @Inject constructor(
    private val signInRepository: SignInRepository
) {
    suspend operator fun invoke(idToken: GoogleIdToken): Result<SignInToken> =
        signInRepository.signInWithGoogleTokenViaServer(idToken)
}