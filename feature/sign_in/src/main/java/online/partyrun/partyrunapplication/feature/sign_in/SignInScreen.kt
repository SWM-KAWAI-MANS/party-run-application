package online.partyrun.partyrunapplication.feature.sign_in

import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import online.partyrun.partyrunapplication.core.designsystem.component.LottieImage
import online.partyrun.partyrunapplication.core.model.auth.GoogleIdToken
import online.partyrun.partyrunapplication.core.ui.GoogleSignInButton
import timber.log.Timber

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    setIntentMainActivity: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.signInGoogleState.collectAsStateWithLifecycle()
    val launcher = managedActivityResultLauncher(viewModel = viewModel)
    var modifierSignIn = Modifier.alpha(1f) // 로그인 진행시 스크린의 투명도 설정, Line: 84

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
                        viewModel.signInWithGoogleTokenViaServer(
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
            viewModel.saveUserData()
        }
    }

    LaunchedEffect(key1 = state.isUserDataSaved) {
        if (state.isUserDataSaved) {
            setIntentMainActivity()
        }
    }

    if (state.isSignInIndicatorOn) {
        modifierSignIn = Modifier.alpha(0.2f)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    Column(
        modifier = modifierSignIn
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(top = 80.dp, bottom = 20.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.logo_content_desc),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            LottieImage(
                modifier = Modifier
                    .size(300.dp),
                rawAnimation = R.raw.running
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GoogleSignInButton(
                /* Firebase Google SignIn process */
                onClick = {
                    viewModel.signInWithGoogle(launcher)
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
    viewModel: SignInViewModel
): ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            viewModel.handleActivityResult(result.resultCode, result.data)
        }
    )
    return launcher
}
