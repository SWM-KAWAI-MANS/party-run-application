package online.partyrun.partyrunapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes

@Composable
fun rememberPartyRunAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): PartyRunAppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        PartyRunAppState(
            navController,
            coroutineScope,
        )
    }
}

@Stable
class PartyRunAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestinations: Set<String>
        get() = setOf(
            MainNavRoutes.Battle.route,
            MainNavRoutes.Challenge.route,
            MainNavRoutes.Party.route,
            MainNavRoutes.Single.route,
            MainNavRoutes.MyPage.route
        )

}

