package online.partyrun.partyrunapplication.core.navigation.main

import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import online.partyrun.partyrunapplication.core.navigation.battle.battleRoute
import online.partyrun.partyrunapplication.core.navigation.battle.battleRunningRoute
import online.partyrun.partyrunapplication.core.navigation.challenge.challengeRoute
import online.partyrun.partyrunapplication.core.navigation.my_page.myPageRoute
import online.partyrun.partyrunapplication.core.navigation.single.singleRoute

@Composable
fun SetUpMainNavGraph(
    navController: NavHostController,
    startDestination: String,
    onSignOut: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        battleRoute(
            navigateToBattleRunningWithArgs = { battleId, runnerIdsJson ->
                navController.navigate("${MainNavRoutes.BattleRunning.route}?battleId=$battleId&runnerIdsJson=$runnerIdsJson")
            }
        )

        singleRoute(
            navigateToMyPage = {
                navController.navigate(MainNavRoutes.Challenge.route) {
                    popUpTo(MainNavRoutes.Challenge.route)
                }
            }
        )

        challengeRoute(
            onSignOut = onSignOut
        )

        myPageRoute()

        battleRunningRoute(
            navigateToBattleOnWebSocketError = {
                navController.navigate(MainNavRoutes.Battle.route) {
                    popUpTo(MainNavRoutes.Battle.route) {
                        inclusive = true
                    }
                }
            }
        )

    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    NavigationBar  {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        BottomNavBarItems.BottomBarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        /*
                        popUpTo(navController.graph.findStartDestination().id) {
                    	    saveState = true
                        }
                         */
                        launchSingleTop = true // 자기 자신이 또 스택 푸시가 되지 않도록 방지
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}
