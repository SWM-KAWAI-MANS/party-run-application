package online.partyrun.partyrunapplication.core.navigation.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.feature.settings.SettingsScreen

fun NavGraphBuilder.settingsRoute() {
    composable(route = MainNavRoutes.Settings.route) {
        SettingsScreen()
    }
}
