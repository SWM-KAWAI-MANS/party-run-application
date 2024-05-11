package online.partyrun.partyrunapplication.core.data.repository

import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.auth.SignInToken

interface SignInRepository {
    /* BaseResponse를 붙이는 경우
    suspend fun signInGoogleTokenToServer(idToken: GoogleIdToken): ApiResponse<BaseResponse<SignInTokenResponse>>
     */
    suspend fun signInWithGoogleTokenViaServer(idToken: GoogleIdToken): Result<SignInToken>

}
