package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.mapResultToDomainModel
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.model.auth.SignInToken
import online.partyrun.partyrunapplication.core.network.datasource.SignInDataSource
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val signInDataSource: SignInDataSource,
) : SignInRepository {

    override suspend fun signInWithGoogleTokenViaServer(idToken: GoogleIdToken): Flow<Result<SignInToken>> =
        apiRequestFlow { signInDataSource.getSignInToken(idToken.toRequestModel()) }
            .mapResultToDomainModel { it.toDomainModel() }

}
