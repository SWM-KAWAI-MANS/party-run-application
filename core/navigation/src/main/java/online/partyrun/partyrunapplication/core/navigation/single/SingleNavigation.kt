package online.partyrun.partyrunapplication.core.navigation.single

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.single.SingleScreen

fun NavGraphBuilder.singleRoute(
    navigateToMyPage: () -> Unit,
) {
    composable(route = MainNavRoutes.Single.route) {
        SingleScreen()
    }
}
