package online.partyrun.partyrunapplication.core.navigation.running_result

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.running_result.BattleResultScreen

fun NavGraphBuilder.runningResultRoute(
    navigateToTopLevel: () -> Unit
) {
    composable(route = MainNavRoutes.BattleResult.route) {
        BattleResultScreen(
            navigateToTopLevel = navigateToTopLevel
        )
    }
}
