package online.partyrun.partyrunapplication.feature.settings

sealed class SettingsScreenState {
    object Main : SettingsScreenState()
    object Unsubscribe : SettingsScreenState()
}
