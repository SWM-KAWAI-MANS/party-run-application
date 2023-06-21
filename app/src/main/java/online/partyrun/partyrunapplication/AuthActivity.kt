package online.partyrun.partyrunapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.core.navigation.auth.AuthNavRoutes
import online.partyrun.partyrunapplication.core.navigation.auth.signInRoute
import online.partyrun.partyrunapplication.core.navigation.auth.splashRoute
import online.partyrun.partyrunapplication.core.network.GoogleAuthUiClient
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    @Inject
    lateinit var googleAuthUiClient: GoogleAuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PartyRunApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    /* 프로세스 절차 중 유저가 로그아웃 한 경우 혹은 refresh가 만료된 경우 Splash 생략하고 바로 sign_in으로 */
                    val fromMain = intent.getStringExtra("fromMain")?: "splash"

                    NavHost(
                        navController = navController,
                        startDestination = fromMain
                    ) {
                        splashRoute(
                            setIntentMainActivity = {
                                if (googleAuthUiClient.getGoogleAuthUser() != null) {
                                    setIntentActivity(
                                        MainActivity::class.java,
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    )
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                    finish()
                                } else { // 유저가 로그아웃 상태라면 로그인부터 진행
                                    navController.navigate(AuthNavRoutes.SignIn.route)
                                }
                            }
                        )

                        signInRoute(
                            googleAuthUiClient = googleAuthUiClient,
                            setIntentMainActivity = {
                                /* TODO: Toast 메시지 변경 요 */
                                Toast.makeText(applicationContext, "Sign in successful", Toast.LENGTH_LONG).show()
                                setIntentActivity(
                                    MainActivity::class.java,
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                )
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // 화면 전환 애니메이션
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}
