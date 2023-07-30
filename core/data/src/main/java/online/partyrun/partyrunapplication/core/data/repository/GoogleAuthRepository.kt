package online.partyrun.partyrunapplication.core.data.repository

import android.content.Intent
import android.content.IntentSender
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserInfo

interface GoogleAuthRepository {
    /**
     * Google Sign
     */
    suspend fun signInGoogle(): IntentSender?
    suspend fun signInGoogleWithIntent(intent: Intent): GoogleUserInfo?
    suspend fun signOutGoogleAuth()
    fun getGoogleAuthUser(): GoogleUserData?
}