package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.network.model.response.SignInTokenResponse
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.request.GoogleIdTokenRequest
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import javax.inject.Inject

class SignInDataSourceImpl @Inject constructor(
    private val signApi: SignInApiService
) : SignInDataSource {
    override suspend operator fun invoke(idToken: GoogleIdTokenRequest): ApiResult<SignInTokenResponse> =
        signApi.signInWithGoogleTokenViaServer(idToken)
}
