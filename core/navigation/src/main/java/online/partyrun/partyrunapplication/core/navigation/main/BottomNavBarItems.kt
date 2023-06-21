package online.partyrun.partyrunapplication.core.navigation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings

object BottomNavBarItems {
    val BottomBarItems = listOf(
        BottomBarItem(
            title = "Battle",
            image = Icons.Filled.Home,
            route = MainNavRoutes.Battle.route
        ),
        BottomBarItem(
            title = "Single",
            image = Icons.Filled.Face,
            route = MainNavRoutes.Single.route
        ),
        BottomBarItem(
            title = "Challenge",
            image = Icons.Filled.Person,
            route = MainNavRoutes.Challenge.route
        ),
        BottomBarItem(
            title = "MyPage",
            image = Icons.Filled.Settings,
            route = MainNavRoutes.MyPage.route
        ),
    )
}
