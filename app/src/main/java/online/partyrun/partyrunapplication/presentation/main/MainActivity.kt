package online.partyrun.partyrunapplication.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.utils.extension.setIntentActivity
import online.partyrun.partyrunapplication.navigation.BottomNavigationBar
import online.partyrun.partyrunapplication.navigation.NavRoutes
import online.partyrun.partyrunapplication.navigation.SetUpNavGraph
import online.partyrun.partyrunapplication.presentation.auth.AuthActivity
import online.partyrun.partyrunapplication.presentation.auth.signin.GoogleAuthUiClient
import online.partyrun.partyrunapplication.presentation.theme.PartyRunApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PartyRunApplicationTheme {
                /**
                 * Navigation Architecture Component
                 * 메인 기준 NavHostController 인스턴스 생성
                 */
                val navController = rememberNavController() // 백 스택 관리 및 현재 목적지가 어떤 Composable인지 추적
                SetUpMainGraph(
                    navController = navController,
                    onSignOut = {
                        lifecycleScope.launch {
                            googleAuthUiClient.signOutGoogleAuth()
                            /*TODO: Toast 메세지 변경 요*/
                            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_LONG).show()

                            /* 로그아웃 한 경우 Splash 생략을 위한 Intent Extension Bundle String 제공*/
                            setIntentActivity(AuthActivity::class.java) {
                                putString("fromSignOut", "sign_in")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpMainGraph(
    navController: NavHostController,
    onSignOut: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            SetUpNavGraph(
                startDestination = NavRoutes.Test1.route,
                navController = navController,
                onSignOut = onSignOut
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PartyRunApplicationTheme {
        /*TODO*/
    }
}
