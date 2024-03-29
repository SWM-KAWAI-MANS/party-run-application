package online.partyrun.partyrunapplication.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import online.partyrun.partyrunapplication.R
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunNavigationBar
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunNavigationBarItem
import online.partyrun.partyrunapplication.core.navigation.battle.battleRoute
import online.partyrun.partyrunapplication.core.navigation.running.battleRunningRoute
import online.partyrun.partyrunapplication.core.navigation.challenge.challengeRoute
import online.partyrun.partyrunapplication.core.navigation.main.BottomNavBarItems
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.core.navigation.my_page.myPageRoute
import online.partyrun.partyrunapplication.core.navigation.party.PartyNavRoutes
import online.partyrun.partyrunapplication.core.navigation.party.partyRoute
import online.partyrun.partyrunapplication.core.navigation.running.singleRunningRoute
import online.partyrun.partyrunapplication.core.navigation.running_result.runningResultRoute
import online.partyrun.partyrunapplication.core.navigation.settings.SettingsNavRoutes
import online.partyrun.partyrunapplication.core.navigation.settings.settingsRoute
import online.partyrun.partyrunapplication.core.navigation.single.singleRoute
import online.partyrun.partyrunapplication.ui.PartyRunAppState

@Composable
fun SetUpMainNavGraph(
    appState: PartyRunAppState,
    startDestination: String,
    onSignOut: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        battleRoute(
            navigateToBattleRunningWithDistance = { distance ->
                navController.navigate("${MainNavRoutes.BattleRunning.route}?distance=$distance") {
                    popUpTo(MainNavRoutes.Battle.route) {
                        inclusive = false
                    }
                }
            },
            onShowSnackbar = onShowSnackbar
        )

        singleRoute(
            navigateToSingleRunningWithDistanceAndTime = { distance, time ->
                navController.navigate("${MainNavRoutes.SingleRunning.route}?distance=$distance&time=$time") {
                    popUpTo(MainNavRoutes.Single.route) {
                        inclusive = false
                    }
                }
            },
            onShowSnackbar = onShowSnackbar
        )

        challengeRoute()

        partyRoute(
            navigateToPartyRoom = { code, hasManagerPrivileges ->
                navController.navigate("${PartyNavRoutes.PartyRoom.route}?code=$code&hasManagerPrivileges=$hasManagerPrivileges") {
                    popUpTo(MainNavRoutes.Party.route) {
                        inclusive = false
                    }
                }
            },
            navigateToParty = {
                navController.navigate(MainNavRoutes.Party.route) {
                    popUpTo(MainNavRoutes.Party.route) {
                        inclusive = true
                    }
                }
            },
            navigateToBattleRunningWithDistance = { distance ->
                navController.navigate("${MainNavRoutes.BattleRunning.route}?distance=$distance") {
                    popUpTo(MainNavRoutes.Party.route) {
                        inclusive = false
                    }
                }
            },
            onShowSnackbar = onShowSnackbar
        )

        myPageRoute(
            navigateToSettings = {
                navController.navigate(MainNavRoutes.Settings.route)
            },
            navigateToMyPage = {
                navController.navigate(MainNavRoutes.MyPage.route) {
                    popUpTo(MainNavRoutes.MyPage.route) {
                        inclusive = true
                    }
                }
            },
            navigateToProfile = {
                navController.navigate(MainNavRoutes.Profile.route)
            },
            navigateToSingleResult = { isFromMyPage ->
                navController.navigate("${MainNavRoutes.SingleResult.route}?isFromMyPage=$isFromMyPage") {
                    popUpTo(MainNavRoutes.MyPage.route) {
                        inclusive = false
                    }
                }
            },
            navigateToBattleResult = { isFromMyPage ->
                navController.navigate("${MainNavRoutes.BattleResult.route}?isFromMyPage=$isFromMyPage") {
                    popUpTo(MainNavRoutes.MyPage.route) {
                        inclusive = false
                    }
                }
            },
            onShowSnackbar = onShowSnackbar
        )

        battleRunningRoute(
            navigateToBattleOnWebSocketError = {
                navController.navigate(MainNavRoutes.Battle.route) {
                    popUpTo(MainNavRoutes.Battle.route) {
                        inclusive = true
                    }
                }
            },
            navigateToBattleResult = {
                navController.navigate(MainNavRoutes.BattleResult.route) {
                    popUpTo(MainNavRoutes.Battle.route) {
                        inclusive = true
                    }
                }
            },
            onShowSnackbar = onShowSnackbar
        )

        singleRunningRoute(
            navigateToSingleResult = {
                navController.navigate(MainNavRoutes.SingleResult.route) {
                    popUpTo(MainNavRoutes.Single.route) {
                        inclusive = true
                    }
                }
            },
            onShowSnackbar = onShowSnackbar
        )

        runningResultRoute(
            navigateToTopLevel = {
                navController.navigate(MainNavRoutes.Battle.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            },
            navigateToBack = {
                navController.popBackStack()
            }
        )

        settingsRoute(
            onSignOut = onSignOut,
            navigateBack = {
                navController.popBackStack()
            },
            navigateToUnsubscribe = {
                navController.navigate(SettingsNavRoutes.Unsubscribe.route)
            },
            onShowSnackbar = onShowSnackbar
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    PartyRunNavigationBar(
        modifier = Modifier,
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        BottomNavBarItems.BottomBarItems.forEach { navItem ->
            PartyRunNavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true // 자기 자신이 또 스택 푸시가 되지 않도록 방지
                        restoreState = true
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (!navItem.isLaunched) {
                                Badge {
                                    Text(text = stringResource(id = R.string.navigation_bar_launch_status))
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(navItem.image),
                            contentDescription = stringResource(navItem.title),
                            tint = Color.Unspecified
                        )
                    }
                },
                selectedIcon = {
                    BadgedBox(
                        badge = {
                            if (!navItem.isLaunched) {
                                Badge {
                                    Text(text = stringResource(id = R.string.navigation_bar_launch_status))
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(navItem.selectedImage),
                            contentDescription = stringResource(navItem.title),
                            tint = Color.Unspecified
                        )
                    }
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
