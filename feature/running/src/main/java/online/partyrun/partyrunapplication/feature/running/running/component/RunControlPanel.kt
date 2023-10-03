package online.partyrun.partyrunapplication.feature.running.running.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunImageButton
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.single.RunningServiceState

@Composable
fun RunControlPanel(
    pausedState: RunningServiceState,
    pauseAction: () -> Unit,
    resumeAction: () -> Unit,
    stopAction: () -> Unit,
) {
    if (pausedState != RunningServiceState.PAUSED) {
        PartyRunImageButton(
            modifier = Modifier.size(80.dp),
            image = R.drawable.pause,
        ) {
            pauseAction()
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            PartyRunImageButton(
                modifier = Modifier.size(80.dp),
                image = R.drawable.restart,
            ) {
                resumeAction()
            }
            PartyRunImageButton(
                modifier = Modifier.size(80.dp),
                image = R.drawable.stop,
            ) {
                stopAction()
            }
        }
    }
}