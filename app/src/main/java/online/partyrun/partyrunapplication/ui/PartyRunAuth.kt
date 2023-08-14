package online.partyrun.partyrunapplication.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunBackground
import online.partyrun.partyrunapplication.core.navigation.auth.AuthNavRoutes
import online.partyrun.partyrunapplication.navigation.SetUpAuthNavGraph

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun PartyRunAuth(
    appState: PartyRunAppState = rememberPartyRunAppState(),
    intentToMainActivity: () -> Unit
) {
    PartyRunBackground {
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                SetUpAuthNavGraph(
                    appState = appState,
                    startDestination = AuthNavRoutes.Splash.route,
                    intentToMainActivity = intentToMainActivity
                )
            }
        }
    }
}
