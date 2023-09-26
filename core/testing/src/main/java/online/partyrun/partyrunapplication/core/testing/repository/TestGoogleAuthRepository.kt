package online.partyrun.partyrunapplication.core.testing.repository

import android.content.Intent
import android.content.IntentSender
import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.data.repository.GoogleAuthRepository
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserInfo
import online.partyrun.partyrunapplication.core.common.result.Result

class TestGoogleAuthRepository : GoogleAuthRepository{
    override suspend fun signInGoogle(): Result<IntentSender?> {
        TODO("Not yet implemented")
    }

    override suspend fun signInGoogleWithIntent(intent: Intent): GoogleUserInfo? {
        TODO("Not yet implemented")
    }

    override suspend fun signOutGoogleAuth() {
        TODO("Not yet implemented")
    }

    override fun getGoogleAuthUser(): Flow<GoogleUserData?> {
        TODO("Not yet implemented")
    }

}