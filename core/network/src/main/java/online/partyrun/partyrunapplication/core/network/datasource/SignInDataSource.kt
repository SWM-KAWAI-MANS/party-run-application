package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.signin.SignInTokenResult

interface SignInDataSource {
    suspend operator fun invoke(idToken: GoogleIdToken): ApiResult<SignInTokenResult>
}
