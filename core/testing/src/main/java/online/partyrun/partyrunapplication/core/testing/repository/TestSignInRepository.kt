package online.partyrun.partyrunapplication.core.testing.repository

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.auth.SignInToken

class TestSignInRepository : SignInRepository {
    private val tokenSet = MutableSharedFlow<ApiResponse<SignInToken>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * 테스트용 데이터를 방출하는 메소드
     */
    suspend fun emit(value: ApiResponse<SignInToken>) {
        tokenSet.emit(value)
    }

    override suspend fun signInWithGoogleTokenViaServer(idToken: GoogleIdToken): Flow<ApiResponse<SignInToken>> = tokenSet
}
