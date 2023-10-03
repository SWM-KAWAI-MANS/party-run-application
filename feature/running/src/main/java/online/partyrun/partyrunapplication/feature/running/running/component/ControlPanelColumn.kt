package online.partyrun.partyrunapplication.feature.running.running.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.running.FixedWidthTimeText
import online.partyrun.partyrunapplication.feature.running.single.SingleContentUiState
import online.partyrun.partyrunapplication.feature.running.single.SingleContentViewModel
import online.partyrun.partyrunapplication.feature.running.single.getTimeComponents

@Composable
fun ControlPanelColumn(
    singleContentUiState: SingleContentUiState,
    singleContentViewModel: SingleContentViewModel,
    openRunningExitDialog: MutableState<Boolean>
) {
    RunningMetricsPanel(
        title = stringResource(id = R.string.progress_time)
    ) {
        val (hours, minutes, seconds) = singleContentUiState.getTimeComponents()
        FixedWidthTimeText(hours, minutes, seconds)
    }
    Spacer(modifier = Modifier.size(5.dp))
    RunControlPanel(
        pausedState = singleContentUiState.runningServiceState,
        pauseAction = {
            singleContentViewModel.pauseSingleRunningService(isUserPaused = true)
        },
        resumeAction = {
            singleContentViewModel.resumeSingleRunningService()
        },
        stopAction = {
            openRunningExitDialog.value = true
        }
    )
}
