package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.GoogleIdTokenRequest
import online.partyrun.partyrunapplication.core.network.model.response.SignInTokenResponse

interface SignInDataSource {
    suspend fun getSignInToken(idToken: GoogleIdTokenRequest): ApiResponse<SignInTokenResponse>
}
