package online.partyrun.partyrunapplication.core.navigation.running

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.running.battle.BattleContentScreen
import online.partyrun.partyrunapplication.feature.running.single.SingleContentScreen

fun NavGraphBuilder.battleRunningRoute(
    navigateToBattleOnWebSocketError: () -> Unit,
    navigationToRunningResult: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    composable(
        route = "${MainNavRoutes.BattleRunning.route}?distance={distance}",
        arguments = listOf(navArgument("distance") {
            type = NavType.IntType
            defaultValue = 1000 // 기본 값을 1km로 설정
        })
    ) { backStackEntry ->
        BattleContentScreen(
            targetDistance = backStackEntry.arguments?.getInt("distance"),
            navigateToBattleOnWebSocketError = navigateToBattleOnWebSocketError,
            navigationToRunningResult = navigationToRunningResult,
            onShowSnackbar = onShowSnackbar
        )
    }
}

fun NavGraphBuilder.singleRunningRoute(
    onShowSnackbar: (String) -> Unit
) {
    composable(
        route = "${MainNavRoutes.SingleRunning.route}?distance={distance}&time={time}",
        arguments = listOf(
            navArgument("distance") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument("time") {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ) { backStackEntry ->
        val distance = backStackEntry.arguments?.getInt("distance")
        val time = backStackEntry.arguments?.getInt("time")
        SingleContentScreen(
            targetDistance = distance,
            targetTime = time,
            onShowSnackbar = onShowSnackbar
        )
    }
}
