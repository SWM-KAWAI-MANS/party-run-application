package online.partyrun.partyrunapplication.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunBackground
import online.partyrun.partyrunapplication.core.navigation.auth.AuthNavRoutes
import online.partyrun.partyrunapplication.core.ui.SnackbarBox
import online.partyrun.partyrunapplication.navigation.SetUpAuthNavGraph

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun PartyRunAuth(
    appState: PartyRunAppState = rememberPartyRunAppState(),
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    intentToMainActivity: () -> Unit
) {
    PartyRunBackground {
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    snackbarHostState,
                    snackbar = {
                        SnackbarBox(it) {
                            snackbarHostState.currentSnackbarData?.dismiss()
                        }
                    }
                )
            },
        ) { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .alpha(if (snackbarHostState.currentSnackbarData != null) 0.3f else 1f)
                    .padding(padding)
            ) {
                SetUpAuthNavGraph(
                    appState = appState,
                    startDestination = AuthNavRoutes.Splash.route,
                    intentToMainActivity = intentToMainActivity,
                    onShowSnackbar = { message ->
                        snackbarScope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }
        }
    }
}
