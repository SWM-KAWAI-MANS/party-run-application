package online.partyrun.partyrunapplication.feature.battle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BattleMainScreen(
    battleViewModel: BattleViewModel = hiltViewModel(),
    matchViewModel: MatchViewModel = hiltViewModel()
) {
    val waitingRunnerEventState by matchViewModel.waitingRunnerEventState.collectAsState()
    val matchResultEventState by matchViewModel.matchResultEventState.collectAsState()

    Content(matchViewModel, waitingRunnerEventState, matchResultEventState)
}

@Composable
fun Content(
    viewModel: MatchViewModel,
    waitingRunnerState: WaitingRunnerState,
    matchResultEventState: MatchResultState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(50.dp))
        MatchContent(
            mainTitle = "매치 인원 탐색",
            connectTitle = "인원 탐색 시작",
            closeTitle = "인원 탐색 종료",
            connectEvent = { viewModel.connectMatchEventSource() },
            closeEvent = { viewModel.closeMatchEventSource() }
        )
        Text(
            text = "연결 여부: " + waitingRunnerState.isSuccess
        )
        Text(
            text = "메세지: " + waitingRunnerState.message
        )
        Spacer(modifier = Modifier.size(50.dp))
        MatchContent(
            mainTitle = "파티 생성",
            connectTitle = "파티 생성 수락",
            closeTitle = "파티 생성 거절",
            connectEvent = { viewModel.connectMatchResultEventSource() },
            closeEvent = { viewModel.closeMatchResultEventSource() }
        )
        Text(
            text = "상태: " + matchResultEventState.status
        )
        Text(
            text = "생성된 방 엔드포인트: " + matchResultEventState.location
        )
    }
}

@Composable
fun MatchContent(
    mainTitle: String,
    connectTitle: String,
    closeTitle: String,
    connectEvent: () -> Unit,
    closeEvent: () -> Unit
) {
    Row(
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = mainTitle
            )
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                modifier = Modifier
            ) {
                Button(
                    onClick = { connectEvent() }
                ) {
                    Text(connectTitle)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { closeEvent() }
                ) {
                    Text(closeTitle)
                }
            }
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}