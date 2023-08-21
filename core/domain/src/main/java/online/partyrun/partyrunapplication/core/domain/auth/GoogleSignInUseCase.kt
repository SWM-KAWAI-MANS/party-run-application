package online.partyrun.partyrunapplication.core.domain.auth

import android.content.Intent
import android.content.IntentSender
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.GoogleAuthRepository
import online.partyrun.partyrunapplication.core.model.auth.GoogleUserInfo
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) {
    suspend fun signInGoogle(): Result<IntentSender?> {
        return googleAuthRepository.signInGoogle()
    }

    suspend fun signInGoogleWithIntent(intent: Intent): GoogleUserInfo? {
        return googleAuthRepository.signInGoogleWithIntent(intent)
    }

}