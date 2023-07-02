package online.partyrun.partyrunapplication.feature.sign_in

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.designsystem.component.CenterCircularProgressIndicator
import online.partyrun.partyrunapplication.core.model.signin.GoogleIdToken
import online.partyrun.partyrunapplication.core.network.GoogleAuthUiClient
import timber.log.Timber

@Composable
fun SignInScreen (
    viewModel: SignInViewModel = hiltViewModel(),
    googleAuthUiClient: GoogleAuthUiClient,
    setIntentMainActivity: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.signInGoogleState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            /* TODO: Toast 메세지 변경 요 */
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

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

    LaunchedEffect(key1 = state.sendIdTokenToServer) {
        if (state.sendIdTokenToServer) {
            viewModel.resetState()
            setIntentMainActivity()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.onSignInIndicator) {
            CenterCircularProgressIndicator()
        }

        Button(
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
            Text(text = stringResource(id = R.string.sign_in_button_title))
        }
    }
}
