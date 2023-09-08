package online.partyrun.partyrunapplication.feature.running.running

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientText
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunOutlinedText
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.battle.RunnerStatus
import online.partyrun.partyrunapplication.core.ui.BackgroundBlurImage
import online.partyrun.partyrunapplication.core.ui.FormatRunningElapsedTimer
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.battle.BattleContentViewModel
import online.partyrun.partyrunapplication.feature.running.battle.BattleContentUiState
import online.partyrun.partyrunapplication.feature.running.running.component.BattleRunnerMarker
import online.partyrun.partyrunapplication.feature.running.running.component.RunnerGraphic
import online.partyrun.partyrunapplication.feature.running.running.component.RunningTopAppBar
import online.partyrun.partyrunapplication.feature.running.running.component.RunningTrack
import online.partyrun.partyrunapplication.feature.running.running.component.TrackDistanceDistanceBox
import online.partyrun.partyrunapplication.feature.running.running.component.trackRatio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleRunningScreen(
    battleContentUiState: BattleContentUiState,
    openRunningExitDialog: MutableState<Boolean>,
    battleContentViewModel: BattleContentViewModel = hiltViewModel()
) {
    val battleState = battleContentUiState.battleState // 배틀 상태 state

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
                    totalTrackDistance = battleContentUiState.selectedDistance
                )
            }
            Spacer(modifier = Modifier.size(15.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TrackWithMultipleUsers(
                        battleContentViewModel = battleContentViewModel,
                        totalTrackDistance = battleContentUiState.selectedDistance,
                        userId = battleContentUiState.userId,
                        runners = battleState.battleInfo
                    )
                }
            }
            Spacer(modifier = Modifier.size(15.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FormatRunningElapsedTimer(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                        Row(
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = PartyRunIcons.Schedule),
                                contentDescription = stringResource(id = R.string.schedule_desc),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                            Text(
                                modifier = Modifier.padding(start = 5.dp, bottom = 2.dp),
                                text = stringResource(id = R.string.progress_time),
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        UserDistanceDisplay(battleContentViewModel)
                        Row(
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = PartyRunIcons.Pace),
                                contentDescription = stringResource(id = R.string.pace_desc),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                            Text(
                                modifier = Modifier.padding(start = 3.dp, bottom = 2.dp),
                                text = stringResource(id = R.string.running_distance),
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp)
                ) {
                    // 1등부터 보여주기 위해 오름차순 정렬
                    val sortedRunners = battleState.battleInfo.sortedBy { it.currentRank }
                    LazyColumn {
                        items(sortedRunners) { runnerData ->
                            RealtimeBattleScreenItem(runner = runnerData)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserDistanceDisplay(battleContentViewModel: BattleContentViewModel) {
    Text(
        text = battleContentViewModel.getRunnerDistance(),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
private fun TrackWithMultipleUsers(
    battleContentViewModel: BattleContentViewModel,
    totalTrackDistance: Int,
    userId: String,
    runners: List<RunnerStatus>
) {
    var showArrivalFlag by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(35000)
        showArrivalFlag = true
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val context = LocalContext.current
        val (trackWidth, trackHeight) = trackRatio(context) // 트랙 이미지의 실제 비율에 대한 컴포넌트 크기

        // 트랙 이미지 표시
        RunningTrack(showArrivalFlag)

        // 각 유저의 위치 표시
        runners.forEach { runner ->
            val (currentX, currentY) = battleContentViewModel.mapDistanceToCoordinates(
                totalTrackDistance = totalTrackDistance.toDouble(),
                distance = runner.distance,
                trackWidth = trackWidth,
                trackHeight = trackHeight
            )
            val zIndex =
                if (runner.runnerId == userId) 1f else 0f // 해당 runnerId가 userId와 같으면 z-index를 1로 설정

            RunnerGraphic(
                currentX = currentX,
                currentY = currentY - 50, // 네임과 마커 프레임의 합 height가 110이므로 중간에 맞춰주기 위한 -50 오차보정
                zIndex = zIndex,
                runnerNameContent = {
                    Text(
                        text = runner.runnerName,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                runnerMarker = {
                    BattleRunnerMarker(runner)
                }
            )
        }
    }
}

@Composable
fun RealtimeBattleScreenItem(runner: RunnerStatus) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .shadow(4.dp, shape = RoundedCornerShape(35.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(35.dp)
            ) // 테두리 추가
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(35.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (runner.currentRank == 1) {
                PartyRunGradientText(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp)
                ) {
                    Text(
                        text = "${runner.currentRank} " + stringResource(id = R.string.ranking),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                PartyRunOutlinedText(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                    containerColor = Color.Transparent
                ) {
                    Text(
                        text = "${runner.currentRank} " + stringResource(id = R.string.ranking),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.size(15.dp))
            Text(
                text = runner.runnerName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun RunnerImagePreview(
    imageUrl: String = "https://avatars.githubusercontent.com/u/134378498?s=400&u=72e57bdb2eafcad3d0c8b8e137349397eefce35f&v=4" // 대체 이미지 URL
) {
    Box() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(40.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp) // 모서리를 둥글게 하기 위한 값
                    )
                    .padding(10.dp)
            ) {
                Text(
                    text = "김말숙",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.runner_img_marker),
                    contentDescription = stringResource(id = R.string.runner_img_marker)
                )
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .offset(y = (-6).dp)
                        .clip(CircleShape)
                ) {
                    RenderAsyncUrlImage(
                        imageUrl = imageUrl,
                        contentDescription = null
                    )
                }
            }
        }
    }
}