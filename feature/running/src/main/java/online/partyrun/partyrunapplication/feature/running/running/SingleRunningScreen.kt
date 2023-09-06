package online.partyrun.partyrunapplication.feature.running.running

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.core.model.single.SingleRunnerStatus
import online.partyrun.partyrunapplication.core.ui.BackgroundBlurImage
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.running.component.SingleRunnerMarker
import online.partyrun.partyrunapplication.feature.running.running.component.TrackDistanceDistanceBox
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
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        BackgroundBlurImage(
            modifier = Modifier.fillMaxSize(),
            image = R.drawable.background_running_blur,
            contentAlignment = Alignment.BottomCenter
        )
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
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                TrackWithUserAndRobot(
                    singleContentViewModel = singleContentViewModel,
                    targetDistance = singleContentUiState.selectedDistance,
                    user = singleContentUiState.userState,
                    robot = singleContentUiState.robotState
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RunningMetricsPanel(
                        record = singleContentUiState.distanceInKm,
                        title = stringResource(id = R.string.Kilometer)
                    )
                    RunningMetricsPanel(
                        record = "대체",
                        title = stringResource(id = R.string.pace)
                    )
                }
                RunningMetricsPanel(
                    record = singleContentUiState.elapsedFormattedTime,
                    title = stringResource(id = R.string.progress_time)
                )
                Spacer(modifier = Modifier.size(5.dp))
                PartyRunImageButton(
                    modifier = Modifier.size(80.dp),
                    image = R.drawable.stop,
                ) {
                    openRunningExitDialog.value = true
                }
            }
        }
    }
}

@Composable
private fun RunningMetricsPanel(
    record: String,
    title: String
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = record,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun TrackWithUserAndRobot(
    singleContentViewModel: SingleContentViewModel,
    targetDistance: Int,
    user: SingleRunnerStatus,
    robot: SingleRunnerStatus
) {
    var showArrivalFlag by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(35000)
        showArrivalFlag = true
    }

    // 트랙 이미지 리소스 사용
    val trackImage =
        if (showArrivalFlag) {
            painterResource(R.drawable.running_track_arrival)
        } else {
            painterResource(R.drawable.running_track)
        }

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val context = LocalContext.current
        val imageBitmap = ImageBitmap.imageResource(context.resources, R.drawable.running_track)

        // 트랙 이미지의 실제 비율
        val aspectRatio = imageBitmap.width.toDouble() / imageBitmap.height.toDouble()

        // 컴포넌트의 최대 크기와 비교하여 가로 또는 세로 크기 조절
        val trackWidth = this.maxWidth.value / aspectRatio
        val trackHeight = this.maxHeight.value / aspectRatio

        // 트랙 이미지 표시
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = trackImage,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
                contentDescription = stringResource(id = R.string.track_img)
            )
        }

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
    runner: SingleRunnerStatus,
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

    Box(
        modifier = Modifier
            .offset(
                x = currentX.dp,
                y = currentY.dp
            )
            .zIndex(zIndex)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(40.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(10.dp)
            ) {
                Text(
                    text = runner.runnerName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            SingleRunnerMarker(runner)
        }
    }
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

