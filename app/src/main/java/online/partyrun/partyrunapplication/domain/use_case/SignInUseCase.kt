package online.partyrun.partyrunapplication.domain.use_case

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.data.model.GoogleIdToken
import online.partyrun.partyrunapplication.data.model.SignInTokenResponse
import online.partyrun.partyrunapplication.domain.repository.SignInRepository
import online.partyrun.partyrunapplication.network.ApiResponse
import online.partyrun.partyrunapplication.network.BaseResponse
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val signInRepository: SignInRepository
) {
    suspend fun signInGoogleTokenToServer(idToken: GoogleIdToken): Flow<ApiResponse<SignInTokenResponse>> =
        signInRepository.signInGoogleTokenToServer(idToken)
}
