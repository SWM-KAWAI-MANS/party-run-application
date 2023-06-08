package online.partyrun.partyrunapplication.core.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.feature.signin.GoogleAuthUiClient
import online.partyrun.partyrunapplication.feature.signin.SignInScreen
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