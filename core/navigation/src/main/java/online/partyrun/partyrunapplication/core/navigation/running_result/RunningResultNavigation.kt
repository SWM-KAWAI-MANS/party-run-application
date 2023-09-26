package online.partyrun.partyrunapplication.core.navigation.running_result

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.running_result.battle.BattleResultScreen
import online.partyrun.partyrunapplication.feature.running_result.single.SingleResultScreen

fun NavGraphBuilder.runningResultRoute(
    navigateToTopLevel: () -> Unit
) {
    composable(route = MainNavRoutes.BattleResult.route) {
        BattleResultScreen(
            navigateToTopLevel = navigateToTopLevel
        )
    }
    composable(route = MainNavRoutes.SingleResult.route) {
        SingleResultScreen(
            navigateToTopLevel = navigateToTopLevel
        )
    }
}
