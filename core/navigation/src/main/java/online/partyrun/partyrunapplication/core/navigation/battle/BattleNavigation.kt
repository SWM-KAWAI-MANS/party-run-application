package online.partyrun.partyrunapplication.core.navigation.battle

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import online.partyrun.partyrunapplication.core.model.battle.RunnerIds
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.battle.BattleMainScreen
import online.partyrun.partyrunapplication.feature.running.battle.BattleContentScreen
import java.net.URLDecoder

private const val ARG_BATTLE_ID = "battleId"
private const val ARG_RUNNER_IDS_JSON = "runnerIdsJson"
private const val DEFAULT_VALUE = ""

fun NavGraphBuilder.battleRoute(
    navigateToBattleRunningWithArgs: (String, String) -> Unit
) {
    composable(route = MainNavRoutes.Battle.route) {
        BattleMainScreen(
            navigateToBattleRunningWithArgs = navigateToBattleRunningWithArgs
        )
    }
}
fun NavGraphBuilder.battleRunningRoute(
    navigateToBattleOnWebSocketError: () -> Unit
) {
    composable(
        route = "${MainNavRoutes.BattleRunning.route}?battleId={$ARG_BATTLE_ID}&runnerIdsJson={$ARG_RUNNER_IDS_JSON}",
        arguments = listOf(
            navArgument(ARG_BATTLE_ID) {
                type = NavType.StringType
                defaultValue = DEFAULT_VALUE
            },
            navArgument(ARG_RUNNER_IDS_JSON) {
                type = NavType.StringType
                defaultValue = DEFAULT_VALUE
            }
        )
    ) { backStackEntry ->
        val battleId = backStackEntry.arguments?.getString(ARG_BATTLE_ID)
        val runnerIdsJson = backStackEntry.arguments?.getString(ARG_RUNNER_IDS_JSON) ?: DEFAULT_VALUE
        val decodedRunnerIds = runnerIdsJson.let {
            URLDecoder.decode(it, "UTF-8")
        }
        val runnerIds = decodedRunnerIds?.let {
            Gson().fromJson(it, RunnerIds::class.java)
        } ?: RunnerIds(emptyList())

        BattleContentScreen(
            navigateToBattleOnWebSocketError = navigateToBattleOnWebSocketError,
            battleId = battleId,
            runnerIds = runnerIds
        )
    }
}
