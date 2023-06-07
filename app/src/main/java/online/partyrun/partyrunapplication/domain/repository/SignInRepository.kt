package online.partyrun.partyrunapplication.domain.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.data.model.GoogleIdToken
import online.partyrun.partyrunapplication.data.model.SignInTokenResponse
import online.partyrun.partyrunapplication.network.ApiResponse
import online.partyrun.partyrunapplication.network.BaseResponse

interface SignInRepository {
    suspend fun signInGoogleTokenToServer(idToken: GoogleIdToken): Flow<ApiResponse<SignInTokenResponse>>

}
