package online.partyrun.partyrunapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import online.partyrun.partyrunapplication.core.navigation.auth.AuthNavRoutes
import online.partyrun.partyrunapplication.core.navigation.auth.agreementRoute
import online.partyrun.partyrunapplication.core.navigation.auth.signInRoute
import online.partyrun.partyrunapplication.core.navigation.auth.splashRoute
import online.partyrun.partyrunapplication.ui.PartyRunAppState

@Composable
fun SetUpAuthNavGraph(
    appState: PartyRunAppState,
    startDestination: String,
    intentToMainActivity: () -> Unit
) {
    val navController = appState.navController

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
            navigationToSignIn = {
                navController.navigate(AuthNavRoutes.SignIn.route)
            },
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
