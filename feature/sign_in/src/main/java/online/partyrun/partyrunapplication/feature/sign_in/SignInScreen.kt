package online.partyrun.partyrunapplication.feature.sign_in

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.designsystem.component.LottieImage
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.network.GoogleAuthUiClient
import online.partyrun.partyrunapplication.core.ui.GoogleSignInButton
import timber.log.Timber

@Composable
fun SignInScreen (
    viewModel: SignInViewModel = hiltViewModel(),
    googleAuthUiClient: GoogleAuthUiClient,
    setIntentMainActivity: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.signInGoogleState.collectAsStateWithLifecycle()
    val launcher = managedActivityResultLauncher(googleAuthUiClient, viewModel)
    var modifierSignIn = Modifier.alpha(1f) // 로그인 진행시 스크린의 투명도 설정, Line: 107

    LaunchedEffect(key1 = state.hasSignInError) {
        state.hasSignInError?.let { error ->
            /* TODO: Toast 메세지 변경 요 */
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            val mUser = FirebaseAuth.getInstance().currentUser
            /* TODO: null 처리 */
            mUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken: String? = task.result.token
                        Timber.tag("SignInScreen").i("IDToken: $idToken")
                        /* Send token to backend via HTTPS Retrofit */
                        viewModel.signInGoogleTokenToServer(
                            GoogleIdToken(idToken = idToken)
                        )
                    } else {
                        // Handle error -> task.getException()
                    }
                }
        }
    }

    LaunchedEffect(key1 = state.isIdTokenSentToServer) {
        if (state.isIdTokenSentToServer) {
            viewModel.resetState()
            setIntentMainActivity()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        if (state.isSignInIndicatorOn) {
            modifierSignIn = Modifier.alpha(0.2f)
            CircularProgressIndicator(
                modifier = Modifier.padding(bottom = 110.dp).align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = modifierSignIn
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box() {
                LottieImage(
                    modifier = Modifier
                        .size(270.dp)
                        .clip(RoundedCornerShape(35.dp)),
                    rawAnimation = R.raw.background_effect
                )
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.logo_content_desc),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
            LottieImage(
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(35.dp)),
                rawAnimation = R.raw.running
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GoogleSignInButton(
                /* Firebase Google SignIn process */
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val signInIntentSender = googleAuthUiClient.signInGoogle()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.google_sign_in_button_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun managedActivityResultLauncher(
    googleAuthUiClient: GoogleAuthUiClient,
    viewModel: SignInViewModel
): ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                CoroutineScope(Dispatchers.IO).launch {
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
    return launcher
}
