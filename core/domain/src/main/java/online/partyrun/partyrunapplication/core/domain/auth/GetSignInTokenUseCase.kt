package online.partyrun.partyrunapplication.core.domain.auth

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.signin.SignInToken
import javax.inject.Inject

class GetSignInTokenUseCase @Inject constructor(
    private val signInRepository: SignInRepository
) {
    suspend operator fun invoke(idToken: GoogleIdToken): Flow<ApiResponse<SignInToken>> =
        signInRepository.signInWithGoogleTokenViaServer(idToken)
}