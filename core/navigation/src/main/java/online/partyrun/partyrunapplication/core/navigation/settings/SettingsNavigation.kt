package online.partyrun.partyrunapplication.core.navigation.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.settings.SettingsScreen
import online.partyrun.partyrunapplication.feature.settings.UnsubscribeScreen

fun NavGraphBuilder.settingsRoute(
    onSignOut: () -> Unit,
    navigateBack: () -> Unit,
    navigateToUnsubscribe: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    // MainNavRoute
    composable(route = MainNavRoutes.Settings.route) {
        SettingsScreen(
            navigateBack = navigateBack,
            navigateToUnsubscribe = navigateToUnsubscribe,
            onShowSnackbar = onShowSnackbar
        )
    }

    // SettingsNavRoute
    composable(route = SettingsNavRoutes.Unsubscribe.route) {
        UnsubscribeScreen(
            onSignOut = onSignOut,
            navigateBack = navigateBack,
        )
    }
}
