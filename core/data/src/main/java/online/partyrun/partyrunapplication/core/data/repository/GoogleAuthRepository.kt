package online.partyrun.partyrunapplication.core.data.repository

import android.content.Intent
import android.content.IntentSender
import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserInfo
import online.partyrun.partyrunapplication.core.common.result.Result

interface GoogleAuthRepository {
    /**
     * Google Sign
     */
    suspend fun signInGoogle(): Result<IntentSender?>
    suspend fun signInGoogleWithIntent(intent: Intent): GoogleUserInfo?
    suspend fun signOutGoogleAuth()
    fun getGoogleAuthUser(): Flow<GoogleUserData?>
}