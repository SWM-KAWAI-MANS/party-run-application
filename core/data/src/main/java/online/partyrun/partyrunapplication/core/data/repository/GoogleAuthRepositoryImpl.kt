package online.partyrun.partyrunapplication.core.data.repository

import android.content.Intent
import android.content.IntentSender
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserInfo
import online.partyrun.partyrunapplication.core.network.GoogleAuthClient
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class GoogleAuthRepositoryImpl @Inject constructor(
    private val googleAuthClient: GoogleAuthClient
) : GoogleAuthRepository {
    /**
     * Google Sign
     */
    override suspend fun signInGoogle(): IntentSender? {
        return googleAuthClient.signInGoogle()
    }

    override suspend fun signInGoogleWithIntent(intent: Intent): GoogleUserInfo {
        return googleAuthClient.signInGoogleWithIntent(intent).toDomainModel()
    }

    override suspend fun signOutGoogleAuth() {
        googleAuthClient.signOutGoogleAuth()
    }

    override fun getGoogleAuthUser(): GoogleUserData? {
        return googleAuthClient.getGoogleAuthUser()?.toDomainModel()
    }
}