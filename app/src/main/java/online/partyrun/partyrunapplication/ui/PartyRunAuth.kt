package online.partyrun.partyrunapplication.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunBackground
import online.partyrun.partyrunapplication.navigation.SetUpAuthNavGraph

@Composable
fun PartyRunAuth(
    appState: PartyRunAppState = rememberPartyRunAppState(),
    startDestination: String,
    intentToMainActivity: () -> Unit
) {
    PartyRunBackground {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            SetUpAuthNavGraph(
                appState = appState,
                startDestination = startDestination,
                intentToMainActivity = intentToMainActivity
            )
        }
    }
}
