package online.partyrun.partyrunapplication.core.navigation.main

import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunNavigationBar
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunNavigationBarItem
import online.partyrun.partyrunapplication.core.navigation.battle.battleRoute
import online.partyrun.partyrunapplication.core.navigation.battle_running.battleRunningRoute
import online.partyrun.partyrunapplication.core.navigation.challenge.challengeRoute
import online.partyrun.partyrunapplication.core.navigation.my_page.myPageRoute
import online.partyrun.partyrunapplication.core.navigation.running_result.runningResultRoute
import online.partyrun.partyrunapplication.core.navigation.settings.settingsRoute
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
            navigateToBattleRunning = {
                navController.navigate(MainNavRoutes.BattleRunning.route) {
                    popUpTo(MainNavRoutes.BattleRunning.route)
                }
            }
        )

        singleRoute(
            navigateToMyPage = {
                navController.navigate(MainNavRoutes.Challenge.route) {
                    popUpTo(MainNavRoutes.Challenge.route)
                }
            }
        )

        challengeRoute()

        myPageRoute(
            onSignOut = onSignOut,
            navigateToSettings = {
                navController.navigate(MainNavRoutes.Settings.route)
            }
        )

        battleRunningRoute(
            navigateToBattleOnWebSocketError = {
                navController.navigate(MainNavRoutes.Battle.route) {
                    popUpTo(MainNavRoutes.Battle.route) {
                        inclusive = true
                    }
                }
            },
            navigationToRunningResult = {
                navController.navigate(MainNavRoutes.RunningResult.route) {
                    popUpTo(MainNavRoutes.RunningResult.route) {
                        inclusive = true
                    }
                }
            }
        )

        runningResultRoute()

        settingsRoute(
            navigateBack = {
                navController.popBackStack()
            }
        )

    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    PartyRunNavigationBar(
        modifier = Modifier,
    )  {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        BottomNavBarItems.BottomBarItems.forEach { navItem ->
            PartyRunNavigationBarItem(
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
                        painter = painterResource(navItem.image),
                        contentDescription = stringResource(navItem.title),
                        tint = Color.Unspecified
                    )
                },
                selectedIcon = {
                    Icon(
                        painter = painterResource(navItem.selectedImage),
                        contentDescription = stringResource(navItem.title),
                        tint = Color.Unspecified
                    )
                },
                label = {
                    Text(
                        text = stringResource(navItem.title),
                        color = if (currentRoute == navItem.route) Color.White else Color.Black
                    )
                }
            )
        }
    }
}
