package online.partyrun.partyrunapplication.core.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.network.GoogleAuthUiClient
import online.partyrun.partyrunapplication.feature.sign_in.SignInScreen
import online.partyrun.partyrunapplication.feature.splash.SplashScreen


fun NavGraphBuilder.splashRoute(
    setIntentMainActivity: () -> Unit
) {
    composable(AuthNavRoutes.Splash.route) {
        SplashScreen(
            setIntentMainActivity = setIntentMainActivity
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