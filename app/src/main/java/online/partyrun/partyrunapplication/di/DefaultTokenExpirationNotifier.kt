package online.partyrun.partyrunapplication.di

import android.content.Context
import android.content.Intent
import online.partyrun.partyrunapplication.AuthActivity
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.common.network.TokenExpirationNotifier
import timber.log.Timber

class DefaultTokenExpirationNotifier(
    private val context: Context
): TokenExpirationNotifier {
    override fun onTokenExpired() {
        Timber.tag("DefaultTokenExpirationNotifier").d("refresh token Expired")
        /* 로그아웃 한 경우 Splash 생략을 위한 Intent Extension Bundle String 제공*/
        context.setIntentActivity(
            AuthActivity::class.java,
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        ) {
            putString("fromMain", "sign_in")
        }
    }
}
