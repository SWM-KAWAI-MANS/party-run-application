package online.partyrun.partyrunapplication.data.repository

import online.partyrun.partyrunapplication.data.datasource.SignInDataSource
import online.partyrun.partyrunapplication.data.model.GoogleIdToken
import online.partyrun.partyrunapplication.domain.repository.SignInRepository
import online.partyrun.partyrunapplication.network.apiBaseRequestFlow
import online.partyrun.partyrunapplication.network.apiRequestFlow
import timber.log.Timber
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val signInDataSource: SignInDataSource
): SignInRepository {

    override suspend fun signInGoogleTokenToServer(idToken: GoogleIdToken) = apiBaseRequestFlow {
        signInDataSource.invoke(idToken)
    }
}
