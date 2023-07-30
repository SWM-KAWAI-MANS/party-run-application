package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.request.GoogleIdTokenRequest
import online.partyrun.partyrunapplication.core.network.model.response.SignInTokenResponse

interface SignInDataSource {
    suspend operator fun invoke(idToken: GoogleIdTokenRequest): ApiResult<SignInTokenResponse>
}
