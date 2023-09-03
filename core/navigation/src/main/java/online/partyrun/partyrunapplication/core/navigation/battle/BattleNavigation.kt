package online.partyrun.partyrunapplication.core.navigation.battle

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.battle.BattleScreen

fun NavGraphBuilder.battleRoute(
    navigateToBattleRunningWithDistance: (Int) -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable(route = MainNavRoutes.Battle.route) {
        BattleScreen(
            navigateToBattleRunningWithDistance = navigateToBattleRunningWithDistance,
            onShowSnackbar = onShowSnackbar
        )
    }
}
