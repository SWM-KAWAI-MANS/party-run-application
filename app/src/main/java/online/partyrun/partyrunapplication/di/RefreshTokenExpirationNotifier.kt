package online.partyrun.partyrunapplication.di

import android.content.Context
import android.content.Intent
import online.partyrun.partyrunapplication.AuthActivity
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.common.network.TokenExpirationNotifier
import timber.log.Timber

class RefreshTokenExpirationNotifier(
    private val context: Context
) : TokenExpirationNotifier {

    override fun notifyRefreshTokenExpired() {
        Timber.tag("RefreshTokenExpirationNotifier").d("Refresh token expired")
        /**
         * 리프래시 토큰 만료 시 Auth부터 다시 진행하도록 수행
         */
        context.setIntentActivity(
            AuthActivity::class.java,
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
    }
}
