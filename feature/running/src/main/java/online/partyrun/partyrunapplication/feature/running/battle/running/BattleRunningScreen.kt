package online.partyrun.partyrunapplication.feature.running.battle.running

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientText
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunOutlinedText
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.battle.RunnerStatus
import online.partyrun.partyrunapplication.core.ui.BackgroundBlurImage
import online.partyrun.partyrunapplication.core.ui.FormatRunningElapsedTimer
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.battle.BattleContentViewModel
import online.partyrun.partyrunapplication.feature.running.battle.BattleContentUiState

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RunningTopAppBar(
    openRunningExitDialog: MutableState<Boolean>
) {
    PartyRunTopAppBar(
        modifier = Modifier,
        navigationContent = {
            IconButton(onClick = { openRunningExitDialog.value = true }) {
                Icon(
                    painterResource(id = PartyRunIcons.ArrowBackIos),
                    contentDescription = stringResource(id = R.string.arrow_back_desc)
                )
            }
        },
        actionsContent = {
            IconButton(onClick = { }) {
                Icon(
                    painterResource(id = PartyRunIcons.Menu),
                    contentDescription = stringResource(id = R.string.menu_desc)
                )
            }
        }
    )
}

@Composable
fun TrackWithMultipleUsers(
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
            val (flagX, flagY) = battleContentViewModel.mapDistanceToCoordinates(
                totalTrackDistance = totalTrackDistance.toDouble(),
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

                    RunnerMarker(runner)
                }
            }
        }
    }
}

@Composable
private fun RunnerMarker(runner: RunnerStatus) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        // 마커 프레임
        Image(
            modifier = Modifier.size(70.dp),
            painter = painterResource(id = R.drawable.runner_img_marker),
            contentDescription = stringResource(id = R.string.runner_img_marker)
        )
        // 유저 프로필 이미지
        Box(
            modifier = Modifier
                .size(42.dp)
                .offset(y = (-6).dp)
                .clip(CircleShape)
        ) {
            RenderAsyncUrlImage(
                imageUrl = runner.runnerProfile,
                contentDescription = null
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

@Composable
fun TrackDistanceDistanceBox(
    modifier: Modifier = Modifier,
    totalTrackDistance: Int
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = modifier
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.distance_display),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 30.dp, bottom = 5.dp),
            text = "$totalTrackDistance" + "m",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
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