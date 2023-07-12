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
            navigateToSingle = {
                navController.navigate(MainNavRoutes.Single.route)
            },
            /**
             * 선택적 매개변수는 URL이 "/arg1=$value1/arg2=$value2" 형식이 아닌
             * "?arg1=$value1&arg2=$value2" 형식을 사용하는 경우에만 작동
             */
            navigateToSingleWithArgs = { it ->
                navController.navigate("${MainNavRoutes.Single.route}?userName=$it")
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
