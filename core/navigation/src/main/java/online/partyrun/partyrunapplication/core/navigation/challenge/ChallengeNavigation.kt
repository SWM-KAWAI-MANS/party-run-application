package online.partyrun.partyrunapplication.core.navigation.challenge

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.challenge.ChallengeScreen

fun NavGraphBuilder.challengeRoute(
    onSignOut: () -> Unit
) {
    composable(route = MainNavRoutes.Challenge.route) {
        ChallengeScreen(
            onSignOut = onSignOut
        )
    }
}
