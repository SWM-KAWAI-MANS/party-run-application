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
    intentToMainActivity: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        splashRoute(
            setIntentMainActivity = intentToMainActivity,
            navigateToAgreement = {
                navController.navigate(AuthNavRoutes.Agreement.route)
            }
        )

        agreementRoute(
            navController = navController,
            navigateToSignIn = {
                navController.navigate(AuthNavRoutes.SignIn.route)
            },
            navigateToTermsOfService = {
                navController.navigate(AuthNavRoutes.TermsOfService.route)
            },
            navigateToPrivacyPolicy = {
                navController.navigate(AuthNavRoutes.PrivacyPolicy.route)
            }
        )

        signInRoute(
            setIntentMainActivity = intentToMainActivity,
            onShowSnackbar = onShowSnackbar
        )
    }
}
