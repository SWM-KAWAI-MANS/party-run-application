package online.partyrun.partyrunapplication.core.data.repository

import online.partyrun.partyrunapplication.core.data.datasource.SignInDataSource
import online.partyrun.partyrunapplication.core.model.GoogleIdToken
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val signInDataSource: SignInDataSource
): SignInRepository {

    override suspend fun signInGoogleTokenToServer(idToken: GoogleIdToken) = apiRequestFlow {
        signInDataSource.invoke(idToken)
    }
}
