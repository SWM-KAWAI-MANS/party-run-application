package online.partyrun.partyrunapplication.core.navigation.battle

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.battle.BattleMainScreen

fun NavGraphBuilder.battleRoute(
    navigateToSingle: () -> Unit,
    navigateToSingleWithArgs: (String) -> Unit
) {
    composable(route = MainNavRoutes.Battle.route) {
        BattleMainScreen()
    }
}
