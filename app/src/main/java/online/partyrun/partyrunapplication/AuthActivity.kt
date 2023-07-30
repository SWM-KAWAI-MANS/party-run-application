package online.partyrun.partyrunapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import online.partyrun.partyrunapplication.core.common.ExtraConstants.EXTRA_FROM_MAIN
import online.partyrun.partyrunapplication.core.common.ExtraConstants.SPLASH
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunBackground
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.ui.PartyRunAuth

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PartyRunApplicationTheme(
                darkTheme = isSystemInDarkTheme(),
                androidTheme = false,
                disableDynamicTheming = true,
            ) {
                val navController = rememberNavController()
                /* 프로세스 절차 중 유저가 로그아웃 한 경우 혹은 refresh가 만료된 경우 Splash 생략하고 바로 sign_in으로 */
                val fromMain = intent.getStringExtra(EXTRA_FROM_MAIN)?: SPLASH

                PartyRunBackground {
                    PartyRunAuth(
                        navController = navController,
                        startDestination = fromMain,
                        intentToMainActivity = {
                            intentToMainActivity(toastText = resources.getString(R.string.sign_in_message))
                        }
                    )
                }
            }
        }
    }

    private fun intentToMainActivity(toastText: String) {
        /* TODO: ToastMesseage를 마지막 접속 시간으로 */
        // Toast.makeText(applicationContext, toastText, Toast.LENGTH_LONG).show()
        setIntentActivity(
            MainActivity::class.java,
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // 화면 전환 애니메이션
        finish()
    }
}
