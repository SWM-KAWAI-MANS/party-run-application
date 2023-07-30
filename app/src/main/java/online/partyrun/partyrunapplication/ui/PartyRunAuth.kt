package online.partyrun.partyrunapplication.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.core.navigation.auth.AuthNavRoutes
import online.partyrun.partyrunapplication.core.navigation.auth.agreementRoute
import online.partyrun.partyrunapplication.core.navigation.auth.signInRoute
import online.partyrun.partyrunapplication.core.navigation.auth.splashRoute

@Composable
fun PartyRunAuth(
    navController: NavHostController,
    startDestination: String,
    intentToMainActivity: () -> Unit
) {
    PartyRunApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            SetUpAuthNavGraph(
                navController = navController,
                startDestination = startDestination,
                intentToMainActivity = intentToMainActivity
            )
        }
    }
}

@Composable
fun SetUpAuthNavGraph(
    navController: NavHostController,
    startDestination: String,
    intentToMainActivity: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        splashRoute(
            setIntentMainActivity = intentToMainActivity,
            navigationToAgreement = {
                navController.navigate(AuthNavRoutes.Agreement.route)
            }
        )

        agreementRoute(
            navController = navController,
            setIntentMainActivity = intentToMainActivity,
            navigationToTermsOfService = {
                navController.navigate(AuthNavRoutes.TermsOfService.route)
            },
            navigationToPrivacyPolicy = {
                navController.navigate(AuthNavRoutes.PrivacyPolicy.route)
            }
        )

        signInRoute(
            setIntentMainActivity = intentToMainActivity
        )
    }
}
