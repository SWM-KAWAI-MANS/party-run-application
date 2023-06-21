package online.partyrun.partyrunapplication.core.domain

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.SignInTokenResponse
import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val signInRepository: SignInRepository
) {
    suspend fun signInGoogleTokenToServer(idToken: GoogleIdToken): Flow<ApiResponse<SignInTokenResponse>> =
        signInRepository.signInGoogleTokenToServer(idToken)
}
