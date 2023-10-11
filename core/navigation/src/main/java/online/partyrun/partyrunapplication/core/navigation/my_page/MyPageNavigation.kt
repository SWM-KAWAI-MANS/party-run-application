package online.partyrun.partyrunapplication.core.navigation.my_page

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.my_page.MyPageScreen
import online.partyrun.partyrunapplication.feature.my_page.profile.ProfileScreen

fun NavGraphBuilder.myPageRoute(
    navigateToSettings: () -> Unit,
    navigateToMyPage: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSingleResult: (Boolean) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    composable(route = MainNavRoutes.MyPage.route) {
        MyPageScreen(
            navigateToSettings = navigateToSettings,
            navigateToProfile = navigateToProfile,
            navigateToSingleResult = navigateToSingleResult,
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
