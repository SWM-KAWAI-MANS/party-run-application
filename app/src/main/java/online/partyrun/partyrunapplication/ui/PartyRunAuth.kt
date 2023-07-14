package online.partyrun.partyrunapplication.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import online.partyrun.partyrunapplication.core.common.ExtraConstants
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.core.navigation.auth.signInRoute
import online.partyrun.partyrunapplication.core.navigation.auth.splashRoute
import online.partyrun.partyrunapplication.core.network.GoogleAuthUiClient

@Composable
fun PartyRunAuth(
    navController: NavHostController,
    startDestination: String,
    googleAuthUiClient: GoogleAuthUiClient,
    handleLoginState: () -> Unit,
    intentToMainActivity: () -> Unit
) {
    PartyRunApplicationTheme() {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SetUpAuthNavGraph(
                navController = navController,
                startDestination = startDestination,
                googleAuthUiClient = googleAuthUiClient,
                handleLoginState = handleLoginState,
                intentToMainActivity = intentToMainActivity
            )
        }
    }
}

@Composable
fun SetUpAuthNavGraph(
    navController: NavHostController,
    startDestination: String,
    googleAuthUiClient: GoogleAuthUiClient,
    handleLoginState: () -> Unit,
    intentToMainActivity: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        splashRoute(
            setIntentMainActivity = handleLoginState
        )

        signInRoute(
            googleAuthUiClient = googleAuthUiClient,
            setIntentMainActivity = intentToMainActivity
        )
    }
}
