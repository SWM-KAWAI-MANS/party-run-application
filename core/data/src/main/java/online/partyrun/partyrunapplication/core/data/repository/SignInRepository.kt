package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.auth.SignInToken

interface SignInRepository {
    /* BaseResponse를 붙이는 경우
    suspend fun signInGoogleTokenToServer(idToken: GoogleIdToken): Flow<ApiResponse<BaseResponse<SignInTokenResponse>>>
     */
    suspend fun signInWithGoogleTokenViaServer(idToken: GoogleIdToken): Flow<Result<SignInToken>>

}
