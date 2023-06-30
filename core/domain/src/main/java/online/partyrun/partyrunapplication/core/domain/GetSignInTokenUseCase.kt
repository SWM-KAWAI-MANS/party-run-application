package online.partyrun.partyrunapplication.core.domain

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.signin.SignInTokenResult
import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import javax.inject.Inject

class GetSignInTokenUseCase @Inject constructor(
    private val signInRepository: SignInRepository
) {
    suspend operator fun invoke(idToken: GoogleIdToken): Flow<ApiResponse<SignInTokenResult>> =
        signInRepository.signInGoogleTokenToServer(idToken)
}
