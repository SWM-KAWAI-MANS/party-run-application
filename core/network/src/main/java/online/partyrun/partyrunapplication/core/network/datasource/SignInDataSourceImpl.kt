package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.network.model.response.SignInTokenResponse
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.request.GoogleIdTokenRequest
import online.partyrun.partyrunapplication.core.network.service.SignInApiService
import javax.inject.Inject

class SignInDataSourceImpl @Inject constructor(
    private val signApi: SignInApiService
) : SignInDataSource {
    /* BaseResponse를 붙이는 경우
    suspend operator fun invoke(idToken: GoogleIdToken): ApiResult<BaseResponse<SignInTokenResponse>> =
        signApi.signInWithGoogle(idToken)
     */

    override suspend operator fun invoke(idToken: GoogleIdTokenRequest): ApiResult<SignInTokenResponse> =
        signApi.signInWithGoogle(idToken)
}
