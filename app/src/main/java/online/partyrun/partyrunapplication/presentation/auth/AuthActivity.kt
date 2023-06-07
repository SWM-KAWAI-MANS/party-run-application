package online.partyrun.partyrunapplication.presentation.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.R
import online.partyrun.partyrunapplication.data.model.GoogleIdToken
import online.partyrun.partyrunapplication.presentation.auth.signin.GoogleAuthUiClient
import online.partyrun.partyrunapplication.presentation.auth.signin.SignInScreen
import online.partyrun.partyrunapplication.presentation.auth.signin.SignInViewModel
import online.partyrun.partyrunapplication.presentation.main.MainActivity
import online.partyrun.partyrunapplication.utils.extension.setIntentActivity
import online.partyrun.partyrunapplication.presentation.auth.splash.SplashScreen
import online.partyrun.partyrunapplication.presentation.theme.PartyRunApplicationTheme
import timber.log.Timber

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel by viewModels<SignInViewModel>()
                    val state by viewModel.signInGoogleState.collectAsStateWithLifecycle()

                    /* TODO: 1. 스타트 프로세스 기준 Navigation Architecture Component 설계
                             2. 각 Screen별 그래프 분할을 위한 NavBuilder 구축
                             3. Screen별 로직들 각 Screen Composable 안으로 리팩토링 필요
                     */

                    val navController = rememberNavController()

                    /* 프로세스 절차 중 유저가 로그아웃 한 경우, Splash 생략 */
                    val fromSignOut = intent.getStringExtra("fromSignOut")?: "splash"

                    NavHost(navController = navController, startDestination = fromSignOut) {
                        composable("splash") {
                            SplashScreen()
                            LaunchedEffect(key1 = Unit) {
                                delay(1500L) // Splash 딜레이
                                if (googleAuthUiClient.getGoogleAuthUser() != null) {
                                    setIntentActivity(MainActivity::class.java)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                    finish()
                                } else { // 유저가 로그아웃 상태라면 로그인부터 진행
                                    navController.navigate("sign_in")
                                }
                            }
                        }

                        composable("sign_in") {
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            val signInResult = googleAuthUiClient.signInGoogleWithIntent(
                                                intent = result.data ?: return@launch,
                                            ) {
                                                viewModel.signInGoogleLoadingIndicator()
                                            }
                                            viewModel.onSignInGoogleResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if(state.isSignInSuccessful) {
                                    val mUser = FirebaseAuth.getInstance().currentUser
                                    /* TODO: null 처리 */
                                    mUser?.getIdToken(true)
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val idToken: String? = task.result.token
                                                Timber.tag("idToken").e(idToken)
                                                /* Send token to backend via HTTPS Retrofit */
                                                viewModel.signInGoogleTokenToServer(
                                                    GoogleIdToken(idToken = idToken)
                                                )
                                            }else {
                                                // Handle error -> task.getException()
                                            }
                                        }
                                }
                            }

                            LaunchedEffect(key1 = state.sendIdTokenToServer) {
                                if(state.sendIdTokenToServer) {
                                    /* TODO: Toast 메시지 변경 요 */
                                    Toast.makeText(applicationContext, "Sign in successful", Toast.LENGTH_LONG).show()
                                    viewModel.resetState()
                                    setIntentActivity(MainActivity::class.java)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) // 화면 전환 애니메이션
                                    finish()
                                }
                            }

                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        val signInIntentSender = googleAuthUiClient.signInGoogle()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
