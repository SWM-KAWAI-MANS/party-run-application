package online.partyrun.partyrunapplication.feature.running.running

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.core.model.single.SingleRunnerDisplayStatus
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.util.RunningConstants.ARRIVAL_FLAG_DELAY
import online.partyrun.partyrunapplication.feature.running.util.RunningConstants.RUNNER_GRAPHIC_Y_OFFSET
import online.partyrun.partyrunapplication.feature.running.running.component.RunnerGraphic
import online.partyrun.partyrunapplication.feature.running.running.component.RunningTrack
import online.partyrun.partyrunapplication.feature.running.running.component.SingleRunnerMarker
import online.partyrun.partyrunapplication.feature.running.running.component.TrackDistanceDistanceBox
import online.partyrun.partyrunapplication.feature.running.running.component.trackRatio
import online.partyrun.partyrunapplication.feature.running.single.RunningServiceState
import online.partyrun.partyrunapplication.feature.running.single.SingleContentUiState
import online.partyrun.partyrunapplication.feature.running.single.SingleContentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleRunningScreen(
    singleContentUiState: SingleContentUiState,
    openRunningExitDialog: MutableState<Boolean>,
    singleContentViewModel: SingleContentViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                TrackDistanceDistanceBox(
                    totalTrackDistance = singleContentUiState.selectedDistance
                )
            }
            Spacer(modifier = Modifier.size(15.dp))
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                TrackWithUserAndRobot(
                    singleContentViewModel = singleContentViewModel,
                    targetDistance = singleContentUiState.selectedDistance,
                    user = singleContentUiState.userStatus,
                    robot = singleContentUiState.robotStatus
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(5.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RunningMetricsPanel(
                        title = stringResource(id = R.string.Kilometer)
                    ) {
                        Text(
                            text = singleContentUiState.distanceInKm,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    RunningMetricsPanel(
                        title = stringResource(id = R.string.pace)
                    ) {
                        Text(
                            text = singleContentUiState.instantPace,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                    .shadow(elevation = 5.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(5.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RunningMetricsPanel(
                    title = stringResource(id = R.string.progress_time)
                ) {
                    Text(
                        text = singleContentUiState.elapsedFormattedTime,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
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
        }
    }
}

@Composable
private fun RunControlPanel(
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

@Composable
private fun RunningMetricsPanel(
    title: String,
    record: @Composable () -> Unit
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        record()
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TrackWithUserAndRobot(
    singleContentViewModel: SingleContentViewModel,
    targetDistance: Int,
    user: SingleRunnerDisplayStatus,
    robot: SingleRunnerDisplayStatus
) {
    var showArrivalFlag by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(ARRIVAL_FLAG_DELAY)
        showArrivalFlag = true
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val context = LocalContext.current
        val (trackWidth, trackHeight) = trackRatio(context) // 트랙 이미지의 실제 비율에 대한 컴포넌트 크기

        // 트랙 이미지 표시
        RunningTrack(showArrivalFlag)

        // 유저 렌더링
        RenderRunner(
            singleContentViewModel,
            targetDistance,
            user,
            trackWidth,
            trackHeight,
            isUser = true
        )

        // 로봇 렌더링
        RenderRunner(
            singleContentViewModel,
            targetDistance,
            robot,
            trackWidth,
            trackHeight,
            isUser = false
        )
    }
}

@Composable
private fun RenderRunner(
    singleContentViewModel: SingleContentViewModel,
    targetDistance: Int,
    runner: SingleRunnerDisplayStatus,
    trackWidth: Double,
    trackHeight: Double,
    isUser: Boolean
) {
    val (currentX, currentY) = singleContentViewModel.mapDistanceToCoordinates(
        totalTrackDistance = targetDistance.toDouble(),
        distance = runner.distance,
        trackWidth = trackWidth,
        trackHeight = trackHeight
    )
    val zIndex = if (isUser) 1f else 0f

    RunnerGraphic(
        currentX = currentX,
        currentY = currentY - RUNNER_GRAPHIC_Y_OFFSET, // 네임과 마커 프레임의 합 height가 110이므로 중간에 맞춰주기 위한 RUNNER_GRAPHIC_Y_OFFSET 오차보정
        zIndex = zIndex,
        runnerNameContent = {
            Text(
                text = runner.runnerName,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        runnerMarker = {
            SingleRunnerMarker(runner)
        }
    )
}

@Composable
fun PartyRunImageButton(
    modifier: Modifier = Modifier,
    image: Int,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = stringResource(id = R.string.image_btn_desc),
        modifier = modifier.clickable {
            onClick()
        }
    )
}
