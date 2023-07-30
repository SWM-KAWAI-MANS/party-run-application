package online.partyrun.partyrunapplication.core.navigation.my_page

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.my_page.MyPageScreen

fun NavGraphBuilder.myPageRoute(
    onSignOut: () -> Unit
) {
    composable(route = MainNavRoutes.MyPage.route) {
        MyPageScreen(
            onSignOut = onSignOut
        )
    }
}
