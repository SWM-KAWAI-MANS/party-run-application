package online.partyrun.partyrunapplication.di

import android.content.Context
import android.content.Intent
import online.partyrun.partyrunapplication.AuthActivity
import online.partyrun.partyrunapplication.core.common.ExtraConstants.EXTRA_FROM_MAIN
import online.partyrun.partyrunapplication.core.common.ExtraConstants.SIGN_IN
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.common.network.RefreshTokenExpirationNotifier
import timber.log.Timber

class RefreshTokenExpirationNotifier(
    private val context: Context
): RefreshTokenExpirationNotifier {

    override fun notifyRefreshTokenExpired() {
        Timber.tag("RefreshTokenExpirationNotifier").d("Refresh token expired")
        context.setIntentActivity(
            AuthActivity::class.java,
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        ) {
            putString(EXTRA_FROM_MAIN, SIGN_IN)
        }
    }

}