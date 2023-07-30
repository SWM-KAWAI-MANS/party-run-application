package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.network.model.response.SignInTokenResponse
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.request.GoogleIdTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 *  Call 클래스 => 명시적으로 Success / Fail을 나눠서 처리
 *  Response 클래스 => 서버에서 Status Code를 받아서 케이스를 나눠 처리
 */
interface SignInApiService {

    /* ApiBaseRespose를 넣는 경우 :
     * ApiResult<BaseResponse<SignInTokenResponse>>
     */
    @POST("/api/auth")
    suspend fun signInWithGoogle(
        @Body body: GoogleIdTokenRequest
    ): ApiResult<SignInTokenResponse>

    @POST("/api/auth/access")
    suspend fun replaceToken(
        @Header("Refresh-Token") token: String,
    ): Response<SignInTokenResponse>
}
