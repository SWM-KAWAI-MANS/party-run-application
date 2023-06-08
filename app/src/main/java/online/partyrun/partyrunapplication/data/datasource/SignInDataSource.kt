package online.partyrun.partyrunapplication.data.datasource

import online.partyrun.partyrunapplication.data.model.GoogleIdToken
import online.partyrun.partyrunapplication.data.model.SignInTokenResponse
import online.partyrun.partyrunapplication.data.model.TestQuestionItem
import online.partyrun.partyrunapplication.network.ApiResult
import online.partyrun.partyrunapplication.network.BaseResponse
import online.partyrun.partyrunapplication.network.service.SignInApiService
import javax.inject.Inject

class SignInDataSource @Inject constructor(
    private val signApi: SignInApiService
) {
    /* BaseResponse를 붙이는 경우
    suspend operator fun invoke(idToken: GoogleIdToken): ApiResult<BaseResponse<SignInTokenResponse>> =
        signApi.signInWithGoogle(idToken)
     */

    suspend operator fun invoke(idToken: GoogleIdToken): ApiResult<SignInTokenResponse> =
        signApi.signInWithGoogle(idToken)
}
