package online.partyrun.partyrunapplication.feature.running.single

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.common.Constants
import online.partyrun.partyrunapplication.core.ui.CountdownDialog
import online.partyrun.partyrunapplication.feature.running.finish.FinishScreen
import online.partyrun.partyrunapplication.feature.running.ready.SingleReadyScreen
import online.partyrun.partyrunapplication.feature.running.running.SingleRunningScreen
import online.partyrun.partyrunapplication.feature.running.service.SingleRunningService

@Composable
fun SingleContentScreen(
    targetDistance: Int?,
    targetTime: Int?,
    navigateToRunningResult: () -> Unit = {},
    singleContentViewModel: SingleContentViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit
) {
    val singleContentUiState by singleContentViewModel.singleContentUiState.collectAsStateWithLifecycle()

    Content(
        targetDistance = targetDistance,
        targetTime = targetTime,
        singleContentViewModel = singleContentViewModel,
        singleContentUiState = singleContentUiState,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun Content(
    targetDistance: Int?,
    targetTime: Int?,
    singleContentViewModel: SingleContentViewModel,
    singleContentUiState: SingleContentUiState,
    onShowSnackbar: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        singleContentViewModel.countDownWhenReady()
    }

    CheckStartTime(singleContentUiState, singleContentViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (singleContentUiState.screenState) {
            is SingleScreenState.Ready -> SingleReadyScreen()
            is SingleScreenState.Running ->
                SingleRunningScreen()

            SingleScreenState.Finish -> FinishScreen()
        }
    }
}

@Composable
private fun CheckStartTime(
    singleContentUiState: SingleContentUiState,
    singleContentViewModel: SingleContentViewModel,
) {
    when (singleContentUiState.timeRemaining) {
        in 1..5 -> CountdownDialog(timeRemaining = singleContentUiState.timeRemaining)
        0 -> StartSingleRunning(singleContentViewModel)
    }
}

@Composable
private fun StartSingleRunning(
    singleContentViewModel: SingleContentViewModel,
) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        setOrDisposeSingleRunning(true, singleContentViewModel, context)

        onDispose {
            setOrDisposeSingleRunning(false, singleContentViewModel, context)
        }
    }
}

private fun setOrDisposeSingleRunning(
    isStarting: Boolean,
    singleContentViewModel: SingleContentViewModel,
    context: Context
) {
    // Foreground Service 시작/중지
    if (isStarting) {
        val intent = Intent(context, SingleRunningService::class.java)
        intent.action = Constants.ACTION_START_RUNNING
        context.startService(intent)
    } else {
        val intent = Intent(context, SingleRunningService::class.java)
        intent.action = Constants.ACTION_STOP_RUNNING
        context.stopService(intent)
    }
}
