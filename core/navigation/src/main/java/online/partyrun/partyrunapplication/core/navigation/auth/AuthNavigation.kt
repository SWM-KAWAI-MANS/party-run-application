package online.partyrun.partyrunapplication.core.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.feature.sign_in.SignInScreen
import online.partyrun.partyrunapplication.feature.splash.agreement.AgreementScreen
import online.partyrun.partyrunapplication.feature.splash.agreement.PrivacyPolicyScreen
import online.partyrun.partyrunapplication.feature.splash.splash.SplashScreen
import online.partyrun.partyrunapplication.feature.splash.agreement.TermsOfServiceScreen

fun NavGraphBuilder.splashRoute(
    setIntentMainActivity: () -> Unit,
    navigateToAgreement: () -> Unit
) {
    composable(AuthNavRoutes.Splash.route) {
        SplashScreen(
            setIntentMainActivity = setIntentMainActivity,
            navigateToAgreement = navigateToAgreement
        )
    }
}

fun NavGraphBuilder.agreementRoute(
    navController: NavHostController,
    navigateToSignIn: () -> Unit,
    navigateToTermsOfService: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit
) {
    composable(AuthNavRoutes.Agreement.route) {
        AgreementScreen(
            navigateToSignIn = navigateToSignIn,
            navigateToTermsOfService = navigateToTermsOfService,
            navigateToPrivacyPolicy = navigateToPrivacyPolicy
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
    setIntentMainActivity: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    composable(route = AuthNavRoutes.SignIn.route) {
        SignInScreen(
            setIntentMainActivity = setIntentMainActivity,
            onShowSnackbar = onShowSnackbar
        )
    }
}
