package online.partyrun.partyrunapplication.feature.running.single

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.finish.FinishScreen
import online.partyrun.partyrunapplication.feature.running.ready.SingleReadyScreen
import online.partyrun.partyrunapplication.feature.running.running.SingleRunningScreen
import online.partyrun.partyrunapplication.feature.running.running.component.RunningExitConfirmationDialog
import online.partyrun.partyrunapplication.feature.running.service.SingleRunningService

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
        openRunningExitDialog = openRunningExitDialog
    )

    /**
     * 목표 거리에 도달했다면, 4초 대기 후 결과 스크린으로 이동
     */
    if (singleContentUiState.isFinished) {
        LaunchedEffect(Unit) {
            delay(4000)
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
    if (singleContentUiState.startRunningService) {
        StartSingleRunning(singleContentViewModel)
    }
}

@Composable
private fun StartSingleRunning(
    singleContentViewModel: SingleContentViewModel
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
        singleContentViewModel.initSingleState() // 사용자와 로봇 데이터 초기화
        val intent = Intent(context, SingleRunningService::class.java)
        intent.action = Constants.ACTION_START_RUNNING
        context.startService(intent)
    } else {
        val intent = Intent(context, SingleRunningService::class.java)
        intent.action = Constants.ACTION_STOP_RUNNING
        context.stopService(intent)
    }
}

@Composable
fun RunningBackNavigationHandler(
    activity: Activity?,
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
        // 액티비티 재시작
        activity?.let {
            val intent = it.intent
            it.startActivity(intent)
            it.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            it.finish()
            it.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}