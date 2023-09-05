package online.partyrun.partyrunapplication.feature.running.running

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import online.partyrun.partyrunapplication.feature.running.running.component.RunningTopAppBar
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
    val singleState = singleContentUiState.singleState // 싱글 상태 state

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RunningTopAppBar(
                openRunningExitDialog = openRunningExitDialog
            )
        }
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
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TrackWithUserAndRobot(
                        singleContentViewModel = singleContentViewModel,
                        targetDistance = singleContentUiState.selectedDistance,
                        userName = singleContentUiState.userName,
                        runners = singleState.singleInfo
                    )
                }
            }

            Spacer(modifier = Modifier.size(15.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {

            }
        }
    }
}

@Composable
private fun TrackWithUserAndRobot(
    singleContentViewModel: SingleContentViewModel,
    targetDistance: Int,
    userName: String,
    runners: List<SingleRunnerStatus>
) {
    var showArrivalFlag by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(35000)
        showArrivalFlag = true
    }

    // 트랙 이미지 리소스 사용
    val trackImage = painterResource(R.drawable.runnning_track)

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val context = LocalContext.current
        val imageBitmap = ImageBitmap.imageResource(context.resources, R.drawable.runnning_track)

        // 트랙 이미지의 실제 비율
        val aspectRatio = imageBitmap.width.toDouble() / imageBitmap.height.toDouble()

        // 컴포넌트의 최대 크기와 비교하여 가로 또는 세로 크기 조절
        val trackWidth = this.maxWidth.value / aspectRatio
        val trackHeight = this.maxHeight.value / aspectRatio

        // 트랙 이미지 표시
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = trackImage,
                modifier = Modifier.fillMaxSize(),
                contentDescription = stringResource(id = R.string.track_img)
            )
        }

        // 도착 깃발 표시
        if (showArrivalFlag) {
            // 도착 깃발 좌표 계산
            val (flagX, flagY) = singleContentViewModel.mapDistanceToCoordinates(
                totalTrackDistance = targetDistance.toDouble(),
                distance = 0.0, // 0m 지점
                trackWidth = trackWidth,
                trackHeight = trackHeight
            )
            Box(
                modifier = Modifier
                    .offset(
                        x = flagX.dp,
                        y = flagY.dp + (0.08 * trackHeight).dp // 깃발이므로 마커 오차보정 결과를 다시 되돌림
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrival_flag),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        // 자신과 로봇의 위치 표시
        runners.forEach { runner ->
            val (currentX, currentY) = singleContentViewModel.mapDistanceToCoordinates(
                totalTrackDistance = targetDistance.toDouble(),
                distance = runner.distance,
                trackWidth = trackWidth,
                trackHeight = trackHeight
            )
            val zIndex =
                if (runner.runnerName == userName) 1f else 0f // 사용자가 항상 위에 위치하도록 고정
            Box(
                modifier = Modifier
                    .offset(
                        x = currentX.dp,
                        y = currentY.dp
                    )
                    .zIndex(zIndex) // 여기에 z-index 설정하여 user라면 최상단에 마커를 위치
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
                                shape = RoundedCornerShape(25.dp) // 모서리를 둥글게 하기 위한 값
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
    }
}
