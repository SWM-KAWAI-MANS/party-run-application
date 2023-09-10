package online.partyrun.partyrunapplication.feature.running.single

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.core.common.Constants
import online.partyrun.partyrunapplication.core.ui.CountdownDialog
import online.partyrun.partyrunapplication.feature.running.finish.FinishScreen
import online.partyrun.partyrunapplication.feature.running.ready.SingleReadyScreen
import online.partyrun.partyrunapplication.feature.running.running.SingleRunningScreen
import online.partyrun.partyrunapplication.feature.running.running.component.RunningExitConfirmationDialog
import online.partyrun.partyrunapplication.feature.running.service.SingleRunningService
import timber.log.Timber

@Composable
fun SingleContentScreen(
    targetDistance: Int?,
    targetTime: Int?,
    singleContentViewModel: SingleContentViewModel = hiltViewModel(),
    navigateToSingleResult: () -> Unit = {},
    onShowSnackbar: (String) -> Unit
) {
    val singleContentUiState by singleContentViewModel.singleContentUiState.collectAsStateWithLifecycle()
    val singleContentSnackbarMessage by singleContentViewModel.snackbarMessage.collectAsStateWithLifecycle()
    val openRunningExitDialog = remember { mutableStateOf(false) }

    Content(
        targetDistance = targetDistance,
        targetTime = targetTime,
        navigateToSingleResult = navigateToSingleResult,
        singleContentViewModel = singleContentViewModel,
        singleContentUiState = singleContentUiState,
        openRunningExitDialog = openRunningExitDialog,
        singleContentSnackbarMessage = singleContentSnackbarMessage,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun Content(
    targetDistance: Int?,
    targetTime: Int?,
    navigateToSingleResult: () -> Unit,
    singleContentViewModel: SingleContentViewModel,
    singleContentUiState: SingleContentUiState,
    openRunningExitDialog: MutableState<Boolean>,
    singleContentSnackbarMessage: String,
    onShowSnackbar: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        singleContentViewModel.countDownWhenReady()
    }

    LaunchedEffect(singleContentSnackbarMessage) {
        if (singleContentSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(singleContentSnackbarMessage)
            singleContentViewModel.clearSnackbarMessage()
        }
    }

    // 사용자가 선택한 거리와 시간을 전달 받아 SingleUiState에 업데이트
    LaunchedEffect(targetDistance, targetTime) {
        targetTime?.also { targetTime ->
            targetDistance?.also { targetDistance ->
                singleContentViewModel.updateSelectedDistanceAndTime(targetDistance, targetTime)
            }
        }
    }

    // 대결 중 BackPressed 수행 시 처리할 핸들러
    RunningBackNavigationHandler(
        activity = activity,
        singleContentViewModel = singleContentViewModel,
        openRunningExitDialog = openRunningExitDialog
    )

    /**
     * 목표 거리에 도달했거나 중간에 종료했다면, 3초 대기 후 결과 스크린으로 이동
     */
    if (singleContentUiState.isFinished) {
        LaunchedEffect(Unit) {
            delay(3000)
            navigateToSingleResult()
        }
    }

    CheckStartTime(singleContentUiState)
    StartRunningService(singleContentUiState, singleContentViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (singleContentUiState.screenState) {
            is SingleScreenState.Ready -> SingleReadyScreen()
            is SingleScreenState.Running ->
                SingleRunningScreen(
                    singleContentUiState = singleContentUiState,
                    openRunningExitDialog = openRunningExitDialog
                )

            SingleScreenState.Finish -> FinishScreen()
        }
    }
}

@Composable
private fun CheckStartTime(
    singleContentUiState: SingleContentUiState
) {
    when (singleContentUiState.timeRemaining) {
        in 1..5 -> CountdownDialog(timeRemaining = singleContentUiState.timeRemaining)
    }
}

@Composable
private fun StartRunningService(
    singleContentUiState: SingleContentUiState,
    singleContentViewModel: SingleContentViewModel,
) {
    val context = LocalContext.current
    val receiver = remember { createBroadcastReceiver(singleContentViewModel) }

    when (singleContentUiState.runningServiceState) {
        RunningServiceState.STARTED -> ControlRunningService(
            singleContentViewModel,
            Constants.ACTION_START_RUNNING,
            receiver,
            context
        )

        RunningServiceState.PAUSED -> ControlRunningService(
            singleContentViewModel,
            Constants.ACTION_PAUSE_RUNNING,
            receiver,
            context
        )

        RunningServiceState.RESUMED -> ControlRunningService(
            singleContentViewModel,
            Constants.ACTION_RESUME_RUNNING,
            receiver,
            context
        )

        RunningServiceState.STOPPED -> ControlRunningService(
            singleContentViewModel,
            Constants.ACTION_STOP_RUNNING,
            receiver,
            context
        )
    }
}

@Composable
private fun ControlRunningService(
    singleContentViewModel: SingleContentViewModel,
    action: String,
    receiver: BroadcastReceiver,
    context: Context
) {

    DisposableEffect(action) {
        if (action == Constants.ACTION_START_RUNNING) {
            initializeStateAndRegisterReceiver(singleContentViewModel, context, receiver)
        }
        val intent = createServiceIntent(context, action)
        context.startService(intent)

        onDispose {
            if (action == Constants.ACTION_STOP_RUNNING) {
                unregisterReceiverAndStopService(context, receiver, intent)
            }
        }
    }
}

private fun createServiceIntent(context: Context, action: String): Intent {
    return Intent(context, SingleRunningService::class.java).apply {
        this.action = action
    }
}

private fun createBroadcastReceiver(singleContentViewModel: SingleContentViewModel): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                RunningServiceState.PAUSED.name -> {
                    singleContentViewModel.pauseSingleRunningService()
                }

                RunningServiceState.RESUMED.name -> {
                    singleContentViewModel.resumeSingleRunningService()
                }
            }
        }
    }
}

private fun initializeStateAndRegisterReceiver(
    viewModel: SingleContentViewModel,
    context: Context,
    receiver: BroadcastReceiver
) {
    viewModel.initSingleState()
    val filter = IntentFilter().apply {
        addAction(RunningServiceState.PAUSED.name)
        addAction(RunningServiceState.RESUMED.name)
    }
    context.registerReceiver(receiver, filter)
}

private fun unregisterReceiverAndStopService(
    context: Context,
    receiver: BroadcastReceiver,
    intent: Intent
) {
    context.unregisterReceiver(receiver)
    context.stopService(intent)
}

@Composable
fun RunningBackNavigationHandler(
    activity: Activity?,
    singleContentViewModel: SingleContentViewModel,
    openRunningExitDialog: MutableState<Boolean>
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    val onBackPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                openRunningExitDialog.value = true
            }
        }
    }

    // lifecycleOwner와 backDispatcher가 변경될 때마다 실행
    DisposableEffect(lifecycleOwner, onBackPressedDispatcher) {
        onBackPressedDispatcher?.addCallback(lifecycleOwner, onBackPressedCallback)
        onDispose {
            onBackPressedCallback.remove()
        }
    }

    RunningExitConfirmationDialog(
        openRunningExitDialog = openRunningExitDialog,
    ) {
        singleContentViewModel.stopSingleRunningService()
        singleContentViewModel.stopRunningProcess()
    }
}
