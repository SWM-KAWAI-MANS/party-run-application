package online.partyrun.partyrunapplication.feature.running_result.battle

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientText
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleResultUiModel
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerStatusUiModel
import online.partyrun.partyrunapplication.feature.running_result.R
import online.partyrun.partyrunapplication.feature.running_result.component.ChartScreen
import online.partyrun.partyrunapplication.feature.running_result.component.FixedBottomNavigationSheet
import online.partyrun.partyrunapplication.feature.running_result.component.ResultLoadFailedBody
import online.partyrun.partyrunapplication.feature.running_result.component.MapWidget
import online.partyrun.partyrunapplication.feature.running_result.component.ResultLoadingBody
import online.partyrun.partyrunapplication.feature.running_result.component.SummaryInfo

@Composable
fun BattleResultScreen(
    modifier: Modifier = Modifier,
    isFromMyPage: Boolean = false,
    battleResultViewModel: BattleResultViewModel = hiltViewModel(),
    navigateToTopLevel: () -> Unit,
    navigateToBack: () -> Unit
) {
    val battleResultUiState by battleResultViewModel.battleResultUiState.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        isFromMyPage = isFromMyPage,
        battleResultUiState = battleResultUiState,
        battleResultViewModel = battleResultViewModel,
        navigateToTopLevel = navigateToTopLevel,
        navigateToBack = navigateToBack
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    isFromMyPage: Boolean,
    battleResultUiState: BattleResultUiState,
    battleResultViewModel: BattleResultViewModel,
    navigateToTopLevel: () -> Unit,
    navigateToBack: () -> Unit
) {
    
    Box(modifier = modifier) {
        when (battleResultUiState) {
            is BattleResultUiState.Loading -> ResultLoadingBody()
            is BattleResultUiState.Success ->
                BattleResultBody(
                    isFromMyPage = isFromMyPage,
                    battleResult = battleResultUiState.battleResult,
                    navigateToTopLevel = navigateToTopLevel,
                    navigateToBack = navigateToBack
                )

            is BattleResultUiState.LoadFailed ->
                ResultLoadFailedBody {
                    battleResultViewModel.getBattleResult()
                }
        }
    }
}

@Composable
private fun BattleResultBody(
    battleResult: BattleResultUiModel,
    isFromMyPage: Boolean,
    navigateToTopLevel: () -> Unit,
    navigateToBack: () -> Unit
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
                        BattleTitleAndDateDisplay(battleResult)

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
                            ChartScreen(runnerStatus = selectedRunner)
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
            FixedBottomNavigationSheet(
                isFromMyPage = isFromMyPage,
                navigateToTopLevel = navigateToTopLevel,
                navigateToBack = navigateToBack
            )
        }
    }
}

@Composable
private fun BattleTitleAndDateDisplay(battleResult: BattleResultUiModel) {
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
fun UserProfiles(
    users: List<RunnerStatusUiModel>,
    onRunnerSelected: (RunnerStatusUiModel) -> Unit // 러너 프로필 클릭 시 호출될 콜백
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
    runner: RunnerStatusUiModel,
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
