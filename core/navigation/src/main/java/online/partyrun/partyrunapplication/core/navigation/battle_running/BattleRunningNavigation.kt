package online.partyrun.partyrunapplication.core.navigation.battle_running

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.running.battle.BattleContentScreen

fun NavGraphBuilder.battleRunningRoute(
    navigateToBattleOnWebSocketError: () -> Unit,
    navigationToRunningResult: () -> Unit
) {
    composable(
        route = MainNavRoutes.BattleRunning.route,
    ) {
        BattleContentScreen(
            navigateToBattleOnWebSocketError = navigateToBattleOnWebSocketError,
            navigationToRunningResult = navigationToRunningResult,
        )
    }
}
