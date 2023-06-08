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
import online.partyrun.partyrunapplication.feature.feature1.Test1Screen
import online.partyrun.partyrunapplication.feature.feature2.Test2Screen
import online.partyrun.partyrunapplication.feature.feature3.Test3Screen
import online.partyrun.partyrunapplication.feature.feature4.Test4Screen

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
        test1Route(
            navigateToTest2 = {
                navController.navigate(MainNavRoutes.Test2.route)
            },
            /**
             * 선택적 매개변수는 URL이 "/arg1=$value1/arg2=$value2" 형식이 아닌
             * "?arg1=$value1&arg2=$value2" 형식을 사용하는 경우에만 작동
             */
            navigateToTest2WithArgs = { it ->
                navController.navigate("${MainNavRoutes.Test2.route}?userName=$it")
            }
        )
        test2Route(
            navigateToTest3 = {
                navController.navigate(MainNavRoutes.Test3.route) {
                    popUpTo(MainNavRoutes.Test3.route)
                }
            }
        )
        test3Route(
            onSignOut = onSignOut
        )
        test4Route()
    }
}

/**
 *그래프의 크기가 커질수록 관리가 필요함 ->
 * 그래프를 여러 메서드로 분할할 수 있도록 Navigation Builder 구축
 */
fun NavGraphBuilder.test1Route(
    navigateToTest2: () -> Unit,
    navigateToTest2WithArgs: (String) -> Unit
) {
    composable(route = MainNavRoutes.Test1.route) {
        Test1Screen(
            navigateToTest2WithArgs = navigateToTest2WithArgs
        )
    }
}

fun NavGraphBuilder.test2Route(
    navigateToTest3: () -> Unit,
) {
    /**
     * 선택적 매개변수는 URL이 "/arg1=$value1/arg2=$value2" 형식이 아닌
     * "?arg1=$value1&arg2=$value2" 형식을 사용하는 경우에만 작동
     */
    composable(
        route = "${MainNavRoutes.Test2.route}?userName={userName}",
        arguments = listOf(navArgument("userName") {
            type = NavType.StringType
            defaultValue = ""
        })
    ) { backStackEntry ->
        Test2Screen(
            navigateToTest3 = navigateToTest3,
            userName = backStackEntry.arguments?.getString("userName")
        )
    }
}

fun NavGraphBuilder.test3Route(
    onSignOut: () -> Unit
) {
    composable(route = MainNavRoutes.Test3.route) {
        Test3Screen(
            onSignOut = onSignOut
        )
    }
}

fun NavGraphBuilder.test4Route() {
    composable(route = MainNavRoutes.Test4.route) {
        Test4Screen()
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
