package online.partyrun.partyrunapplication.feature.running_result.single

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.model.running_result.ui.SingleResultUiModel
import online.partyrun.partyrunapplication.feature.running_result.R
import online.partyrun.partyrunapplication.feature.running_result.component.ChartScreen
import online.partyrun.partyrunapplication.feature.running_result.component.FixedBottomNavigationSheet
import online.partyrun.partyrunapplication.feature.running_result.component.MapWidget
import online.partyrun.partyrunapplication.feature.running_result.component.ResultLoadFailedBody
import online.partyrun.partyrunapplication.feature.running_result.component.ResultLoadingBody
import online.partyrun.partyrunapplication.feature.running_result.component.SummaryInfo

@Composable
fun SingleResultScreen(
    modifier: Modifier = Modifier,
    isFromMyPage: Boolean = false,
    singleResultViewModel: SingleResultViewModel = hiltViewModel(),
    navigateToTopLevel: () -> Unit,
    navigateToBack: () -> Unit
) {
    val singleResultUiState by singleResultViewModel.singleResultUiState.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        isFromMyPage = isFromMyPage,
        singleResultUiState = singleResultUiState,
        singleResultViewModel = singleResultViewModel,
        navigateToTopLevel = navigateToTopLevel,
        navigateToBack = navigateToBack
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    isFromMyPage: Boolean,
    singleResultUiState: SingleResultUiState,
    singleResultViewModel: SingleResultViewModel,
    navigateToTopLevel: () -> Unit,
    navigateToBack: () -> Unit
) {
    Box(modifier = modifier) {
        when (singleResultUiState) {
            is SingleResultUiState.Loading -> ResultLoadingBody()
            is SingleResultUiState.Success ->
                SingleResultBody(
                    singleResult = singleResultUiState.singleResult,
                    isFromMyPage = isFromMyPage,
                    singleResultViewModel = singleResultViewModel,
                    navigateToTopLevel = navigateToTopLevel,
                    navigateToBack = navigateToBack
                )

            is SingleResultUiState.LoadFailed ->
                ResultLoadFailedBody {
                    singleResultViewModel.getSingleResult()
                }
        }
    }
}

@Composable
private fun SingleResultBody(
    singleResult: SingleResultUiModel,
    isFromMyPage: Boolean,
    singleResultViewModel: SingleResultViewModel,
    navigateToTopLevel: () -> Unit,
    navigateToBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        if (!isFromMyPage) { // 러닝이 끝난 후의 기록 조회 상황에서만 기록 최신화
            singleResultViewModel.updateSingleHistory()
        }
    }

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
                    isFromMyPage = isFromMyPage,
                    targetDistanceFormatted = singleResult.targetDistanceFormatted,
                    records = singleResult.singleRunnerStatus.records
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
                        .padding(top = 5.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(
                                topStartPercent = 15,
                                topEndPercent = 15
                            ) // 라운딩 모서리
                        )
                ) {
                    // 메인 정보 디스플레이
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 25.dp, horizontal = 20.dp)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(15.dp))
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(
                                    topStartPercent = 15,
                                    topEndPercent = 15
                                ) // 라운딩 모서리
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            SingleTitleAndDateDisplay(singleResult)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SummaryInfo(singleResult.singleRunnerStatus)
                        }

                        // 분석 차트
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(20.dp),
                        ) {
                            Text(text = stringResource(id = R.string.analysis_chart))
                            ChartScreen(runnerStatus = singleResult.singleRunnerStatus)
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
private fun SingleTitleAndDateDisplay(singleResult: SingleResultUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Text(text = singleResult.singleDate)
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "${stringResource(id = R.string.single_title)} ${singleResult.targetDistanceFormatted}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
