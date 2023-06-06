package online.partyrun.partyrunapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings

object BottomNavBarItems {
    val BottomBarItems = listOf(
        BottomBarItem(
            title = "Test1",
            image = Icons.Filled.Home,
            route = NavRoutes.Test1.route
        ),
        BottomBarItem(
            title = "Test2",
            image = Icons.Filled.Face,
            route = NavRoutes.Test2.route
        ),
        BottomBarItem(
            title = "Test3",
            image = Icons.Filled.Person,
            route = NavRoutes.Test3.route
        ),
        BottomBarItem(
            title = "Test4",
            image = Icons.Filled.Settings,
            route = NavRoutes.Test4.route
        ),
    )
}
