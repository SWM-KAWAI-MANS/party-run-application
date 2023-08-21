package online.partyrun.partyrunapplication.core.data.repository

import android.content.Intent
import android.content.IntentSender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserInfo
import online.partyrun.partyrunapplication.core.network.GoogleAuthClient
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import online.partyrun.partyrunapplication.core.common.result.Result
import javax.inject.Inject

class GoogleAuthRepositoryImpl @Inject constructor(
    private val googleAuthClient: GoogleAuthClient
) : GoogleAuthRepository {
    /**
     * Google Sign
     */
    override suspend fun signInGoogle(): Result<IntentSender?> {
        return googleAuthClient.signInGoogle()
    }

    override suspend fun signInGoogleWithIntent(intent: Intent): GoogleUserInfo {
        return googleAuthClient.signInGoogleWithIntent(intent).toDomainModel()
    }

    override suspend fun signOutGoogleAuth() {
        googleAuthClient.signOutGoogleAuth()
    }

    override fun getGoogleAuthUser(): Flow<GoogleUserData?> {
        return googleAuthClient.getGoogleAuthUser().map { it?.toDomainModel() }
    }
}
