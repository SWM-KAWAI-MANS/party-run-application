package online.partyrun.partyrunapplication.feature.running.running.component

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.common.util.speakTTS
import online.partyrun.partyrunapplication.core.common.util.vibrateSingle
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.running.FixedWidthTimeText
import online.partyrun.partyrunapplication.feature.running.single.SingleContentUiState
import online.partyrun.partyrunapplication.feature.running.single.SingleContentViewModel
import online.partyrun.partyrunapplication.feature.running.single.getTimeComponents

@Composable
fun ControlPanelColumn(
    context: Context,
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
            speakTTS(context, context.getString(R.string.tts_running_pause))
            vibrateSingle(context)
            singleContentViewModel.pauseSingleRunningService(isUserPaused = true)
        },
        resumeAction = {
            speakTTS(context, context.getString(R.string.tts_running_resume))
            vibrateSingle(context)
            singleContentViewModel.resumeSingleRunningService()
        },
        stopAction = {
            openRunningExitDialog.value = true
        }
    )
}
