package online.partyrun.partyrunapplication.core.navigation.main

import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import online.partyrun.partyrunapplication.feature.battle.BattleMainScreen
import online.partyrun.partyrunapplication.feature.single.SingleScreen
import online.partyrun.partyrunapplication.feature.challenge.ChallengeScreen
import online.partyrun.partyrunapplication.feature.my_page.MyPageScreen

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

/**
 *그래프의 크기가 커질수록 관리가 필요함 ->
 * 그래프를 여러 메서드로 분할할 수 있도록 Navigation Builder 구축
 */
fun NavGraphBuilder.battleRoute(
    navigateToSingle: () -> Unit,
    navigateToSingleWithArgs: (String) -> Unit
) {
    composable(route = MainNavRoutes.Battle.route) {
        BattleMainScreen()
    }
}

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

fun NavGraphBuilder.challengeRoute(
    onSignOut: () -> Unit
) {
    composable(route = MainNavRoutes.Challenge.route) {
        ChallengeScreen(
            onSignOut = onSignOut
        )
    }
}

fun NavGraphBuilder.myPageRoute() {
    composable(route = MainNavRoutes.MyPage.route) {
        MyPageScreen()
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
