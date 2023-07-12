package online.partyrun.partyrunapplication.core.navigation.single

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.single.SingleScreen

fun NavGraphBuilder.singleRoute(
    navigateToMyPage: () -> Unit,
) {
    /**
     * 선택적 매개변수는 URL이 "/arg1=$value1/arg2=$value2" 형식이 아닌
     * "?arg1=$value1&arg2=$value2" 형식을 사용하는 경우에만 작동
     */
    composable(
        route = "${MainNavRoutes.Single.route}?userName={userName}",
        arguments = listOf(navArgument("userName") {
            type = NavType.StringType
            defaultValue = ""
        })
    ) { backStackEntry ->
        SingleScreen(
            userName = backStackEntry.arguments?.getString("userName")
        )
    }
}
