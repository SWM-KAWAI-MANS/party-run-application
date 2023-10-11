package online.partyrun.partyrunapplication.core.navigation.running_result

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.running_result.battle.BattleResultScreen
import online.partyrun.partyrunapplication.feature.running_result.single.SingleResultScreen

fun NavGraphBuilder.runningResultRoute(
    navigateToTopLevel: () -> Unit,
    navigateToBack: () -> Unit
) {
    composable(route = MainNavRoutes.BattleResult.route) {
        BattleResultScreen(
            navigateToTopLevel = navigateToTopLevel,
        )
    }
    composable(
        route = "${MainNavRoutes.SingleResult.route}?isFromMyPage={isFromMyPage}",
        arguments = listOf(
            navArgument("isFromMyPage") {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) { backStackEntry ->
        val isFromMyPage = backStackEntry.arguments?.getBoolean("isFromMyPage") ?: false
        SingleResultScreen(
            isFromMyPage = isFromMyPage,
            navigateToTopLevel = navigateToTopLevel,
            navigateToBack = navigateToBack
        )
    }
}
