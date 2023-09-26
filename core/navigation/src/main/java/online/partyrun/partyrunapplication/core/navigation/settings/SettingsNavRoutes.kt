package online.partyrun.partyrunapplication.core.navigation.settings

sealed class SettingsNavRoutes(val route: String) {
    object Unsubscribe: SettingsNavRoutes("unsubscribe")
}
