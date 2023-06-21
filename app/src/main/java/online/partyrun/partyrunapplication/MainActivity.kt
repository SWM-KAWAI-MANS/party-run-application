package online.partyrun.partyrunapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.ui.PartyRunMain
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.core.network.GoogleAuthUiClient
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleAuthUiClient: GoogleAuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PartyRunApplicationTheme {
                PartyRunMain(
                    onSignOut = {
                        lifecycleScope.launch {
                            googleAuthUiClient.signOutGoogleAuth()
                            /*TODO: Toast 메세지 변경 요*/
                            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_LONG).show()

                            /* 로그아웃 한 경우 Splash 생략을 위한 Intent Extension Bundle String 제공*/
                            setIntentActivity(AuthActivity::class.java) {
                                putString("fromMain", "sign_in")
                            }
                            overridePendingTransition(0, 0) // 전환 애니메이션 생략
                            finish()
                        }
                    }
                )
            }
        }
    }
}
