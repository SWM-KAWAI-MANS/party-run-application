package online.partyrun.partyrunapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.ui.PartyRunAuth

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash Screen API
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            PartyRunApplicationTheme(
                darkTheme = isSystemInDarkTheme(),
                androidTheme = false,
                disableDynamicTheming = true,
            ) {
                PartyRunAuth(
                    intentToMainActivity = {
                        intentToMainActivity()
                    }
                )
            }
        }
    }

    private fun intentToMainActivity() {
        setIntentActivity(
            MainActivity::class.java,
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // 화면 전환 애니메이션
        finish()
    }

}
