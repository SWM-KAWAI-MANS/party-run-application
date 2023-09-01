package online.partyrun.partyrunapplication.core.navigation.my_page

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.my_page.MyPageScreen
import online.partyrun.partyrunapplication.feature.my_page.profile.ProfileScreen

fun NavGraphBuilder.myPageRoute(
    onSignOut: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateBack: () -> Unit,
    navigateToMyPage: () -> Unit,
    navigationToProfile: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    composable(route = MainNavRoutes.MyPage.route) {
        MyPageScreen(
            onSignOut = onSignOut,
            navigateToSettings = navigateToSettings,
            navigateBack = navigateBack,
            navigationToProfile = navigationToProfile,
            onShowSnackbar = onShowSnackbar
        )
    }

    composable(route = MainNavRoutes.Profile.route) {
        ProfileScreen(
            navigateToMyPage = navigateToMyPage,
            onShowSnackbar = onShowSnackbar
        )
    }
}
