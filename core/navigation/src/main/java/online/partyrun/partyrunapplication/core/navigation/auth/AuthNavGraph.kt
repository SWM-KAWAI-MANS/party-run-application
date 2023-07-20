package online.partyrun.partyrunapplication.core.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.network.GoogleAuthUiClient
import online.partyrun.partyrunapplication.feature.sign_in.SignInScreen
import online.partyrun.partyrunapplication.feature.splash.agreement.AgreementScreen
import online.partyrun.partyrunapplication.feature.splash.agreement.PrivacyPolicyScreen
import online.partyrun.partyrunapplication.feature.splash.splash.SplashScreen
import online.partyrun.partyrunapplication.feature.splash.agreement.TermsOfServiceScreen

fun NavGraphBuilder.splashRoute(
    setIntentMainActivity: () -> Unit,
    navigationToAgreement: () -> Unit
) {
    composable(AuthNavRoutes.Splash.route) {
        SplashScreen(
            setIntentMainActivity = setIntentMainActivity,
            navigationToAgreement = navigationToAgreement
        )
    }
}

fun NavGraphBuilder.agreementRoute(
    navController: NavHostController,
    setIntentMainActivity: () -> Unit,
    navigationToTermsOfService: () -> Unit,
    navigationToPrivacyPolicy: () -> Unit
) {
    composable(AuthNavRoutes.Agreement.route) {
        AgreementScreen(
            setIntentMainActivity = setIntentMainActivity,
            navigationToTermsOfService = navigationToTermsOfService,
            navigationToPrivacyPolicy = navigationToPrivacyPolicy
        )
    }
    composable(AuthNavRoutes.TermsOfService.route) {
        TermsOfServiceScreen(
            backToAgreementScreen = {
                navController.popBackStack()
            }
        )
    }
    composable(AuthNavRoutes.PrivacyPolicy.route) {
        PrivacyPolicyScreen(
            backToAgreementScreen = {
                navController.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.signInRoute(
    googleAuthUiClient: GoogleAuthUiClient,
    setIntentMainActivity: () -> Unit
) {
    composable(route = AuthNavRoutes.SignIn.route) {
        SignInScreen(
            googleAuthUiClient = googleAuthUiClient,
            setIntentMainActivity = setIntentMainActivity
        )
    }
}
