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
    suspend operator fun invoke(idToken: GoogleIdToken): ApiResult<BaseResponse<SignInTokenResponse>> =
        signApi.signInWithGoogle(idToken)
}
