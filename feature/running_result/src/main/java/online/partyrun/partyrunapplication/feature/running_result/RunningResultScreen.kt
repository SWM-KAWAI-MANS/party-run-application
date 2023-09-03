package online.partyrun.partyrunapplication.feature.running_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientText
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleResultUiModel
import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleRunnerRecordUiModel
import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleRunnerStatusUiModel

@Composable
fun RunningResultScreen(
    modifier: Modifier = Modifier,
    runningResultViewModel: RunningResultViewModel = hiltViewModel(),
    navigateToTopLevel: () -> Unit
) {
    val runningResultUiState by runningResultViewModel.runningResultUiState.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        runningResultUiState = runningResultUiState,
        runningResultViewModel = runningResultViewModel,
        navigateToTopLevel = navigateToTopLevel
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    runningResultUiState: RunningResultUiState,
    runningResultViewModel: RunningResultViewModel,
    navigateToTopLevel: () -> Unit,
) {
    Box(modifier = modifier) {
        when (runningResultUiState) {
            is RunningResultUiState.Loading -> LoadingBody()
            is RunningResultUiState.Success ->
                RunningResultBody(
                    battleResult = runningResultUiState.battleResult,
                    navigateToTopLevel = navigateToTopLevel
                )

            is RunningResultUiState.LoadFailed ->
                LoadFailedBody(
                    runningResultViewModel = runningResultViewModel
                )
        }
    }
}

@Composable
private fun LoadingBody() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LoadFailedBody(
    runningResultViewModel: RunningResultViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PartyRunGradientButton(
            onClick = { runningResultViewModel.getBattleResult() }
        ) {
            Text(
                text = stringResource(id = R.string.re_loading),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun RunningResultBody(
    battleResult: BattleResultUiModel,
    navigateToTopLevel: () -> Unit
) {
    // userID에 해당하는 러너 찾기
    val userStatus = battleResult.battleRunnerStatus.find { it.id == battleResult.userId }

    // 선택된 runner의 상태 정보 관리
    var selectedRunner by remember { mutableStateOf(userStatus) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 구글 맵
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(400.dp) // 지도의 높이는 400dp
            ) {
                MapWidget(
                    targetDistance = battleResult.targetDistance,
                    targetDistanceFormatted = battleResult.targetDistanceFormatted,
                    records = selectedRunner?.records
                )
            }

            // 프레임 컴포넌트
            Column(
                modifier = Modifier
                    .padding(top = 280.dp) // 라운딩 모서리를 위해 지도를 살짝만 가려야 하므로 padding은 280dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 60.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(
                                topStartPercent = 15,
                                topEndPercent = 15
                            ) // 라운딩 모서리
                        )
                ) {
                    // 유저 정보 디스플레이 (LazyRow)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp)
                            .background(color = Color.Transparent)
                            .offset(y = (-40).dp)
                    ) {
                        UserProfiles(
                            users = battleResult.battleRunnerStatus,
                            onRunnerSelected = {  // UserProfile을 클릭할 때 해당 runner의 상태 업데이트
                                selectedRunner = it
                            }
                        )
                    }

                    // 메인 정보 디스플레이
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .offset(y = (-20).dp)
                            .clip(RoundedCornerShape(15.dp))
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(15.dp))
                            .background(color = MaterialTheme.colorScheme.surface)
                    ) {
                        TitleAndDateDisplay(battleResult)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            selectedRunner?.let { SummaryInfo(it) }
                        }

                        // 분석 차트
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(20.dp),
                        ) {
                            Text(text = stringResource(id = R.string.analysis_chart))
                            ChartScreen(selectedRunner = selectedRunner)
                        }
                    }
                    Spacer(modifier = Modifier.size(60.dp)) // 바텀 컴포넌트보다 위에서 보이도록 스페이스 설정
                }
            }
        }
        // 스크린 위치에 상관없이 항상 바텀에 고정으로 보이는 컴포넌트
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 5.dp
        ) {
            FixedBottomNavigationSheet(navigateToTopLevel)
        }
    }
}

@Composable
private fun MapWidget(
    targetDistance: Int?,
    targetDistanceFormatted: String,
    records: List<BattleRunnerRecordUiModel>?,
) {
    val points = records?.map { LatLng(it.latitude, it.longitude) } ?: listOf()
    val centerLatLng = getCenterLatLng(points)

    /*
     * centerLatLng 사용하여 카메라의 초기 위치 및 zoom 설정. -> 1000M면 14.5f https://ai-programmer.tistory.com/2
     * centerLatLng의 변화를 감지하여 cameraPositionState 업데이트
     */
    val zoomValue = getZoomValueForDistance(targetDistance)

    val cameraPositionState: CameraPositionState = rememberCameraPositionState()
    LaunchedEffect(centerLatLng) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(centerLatLng, zoomValue)
    }

    GoogleMap(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true
        ),
        cameraPositionState = cameraPositionState
    ) {
        Polyline(
            points = points,
            color = Color.Red
        )
    }
    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        DistanceBox(targetDistanceFormatted)
    }
}

/**
 * targetDistance 값에 따라 zoom 값 결정
 */
@Composable
private fun getZoomValueForDistance(targetDistance: Int?): Float {
    val zoomValue = when (targetDistance) {
        1000 -> 14.5f
        3000 -> 13.5f
        5000 -> 12.8f
        10000 -> 12f
        else -> 12f  // 기본값 혹은 예기치 않은 값에 대한 대응
    }
    return zoomValue
}

/**
 * cameraPosition를 중앙에 위치시키기 위한 points 중앙 좌표를 구하는 작업 수행
 */
@Composable
private fun getCenterLatLng(points: List<LatLng>): LatLng {
    val startLatLng = points.firstOrNull() ?: LatLng(0.0, 0.0)
    val endLatLng = points.lastOrNull() ?: LatLng(0.0, 0.0)

    val bounds = LatLngBounds.Builder()
        .include(startLatLng)
        .include(endLatLng)
        .build()

    // bounds의 중심점을 구하기
    return bounds.center
}

@Composable
private fun SummaryInfo(
    selectedRunner: BattleRunnerStatusUiModel
) {
    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.time))
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = selectedRunner.elapsedTime,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.avg_pace))
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = selectedRunner.averagePace,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.avg_altitude))
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = selectedRunner.averageAltitude.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun TitleAndDateDisplay(battleResult: BattleResultUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Text(text = battleResult.battleDate)
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "${stringResource(id = R.string.battle_title)} ${battleResult.targetDistanceInKm}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun FixedBottomNavigationSheet(navigateToTopLevel: () -> Unit) {
    Row(
        modifier = Modifier
            .heightIn(50.dp)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        PartyRunGradientButton(
            onClick = { navigateToTopLevel() }
        ) {
            Text(
                text = stringResource(id = R.string.navigate_to_top_level),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun DistanceBox(targetDistance: String) {
    Row(
        modifier = Modifier.padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = PartyRunIcons.DistanceIcon),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .padding(top = 3.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
        )
        Text(
            text = targetDistance, // "X,xxx m" 형식
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun UserProfiles(
    users: List<BattleRunnerStatusUiModel>,
    onRunnerSelected: (BattleRunnerStatusUiModel) -> Unit // 러너 프로필 클릭 시 호출될 콜백
) {
    LazyRow(
        modifier = Modifier.padding(all = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(users) { runner ->
            UserProfile(
                runner = runner,
                onRunnerClick = { onRunnerSelected(runner) } // 선택된 runner 정보로 업데이트
            )
        }
    }
}

@Composable
fun UserProfile(
    runner: BattleRunnerStatusUiModel,
    onRunnerClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onRunnerClick) // 사용자 프로필 클릭 시 runner 정보 업데이트
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            ) {
                RenderAsyncUrlImage(
                    imageUrl = runner.profile,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                Text(
                    text = runner.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = runner.elapsedTime,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 10.dp, y = (-22).dp)
        ) {
            PartyRunGradientText(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${runner.rank} ${stringResource(id = R.string.rank)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
