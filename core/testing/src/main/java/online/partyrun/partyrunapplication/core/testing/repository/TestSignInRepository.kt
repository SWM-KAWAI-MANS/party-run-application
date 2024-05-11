package online.partyrun.partyrunapplication.core.testing.repository

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.auth.SignInToken

class TestSignInRepository : SignInRepository {
    private val tokenSet = MutableSharedFlow<Result<SignInToken>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * 테스트용 데이터를 방출하는 메소드
     */
    suspend fun emit(value: Result<SignInToken>) {
        tokenSet.emit(value)
    }

    override suspend fun signInWithGoogleTokenViaServer(idToken: GoogleIdToken): Result<SignInToken> {
        return tokenSet.first()
    }

}
