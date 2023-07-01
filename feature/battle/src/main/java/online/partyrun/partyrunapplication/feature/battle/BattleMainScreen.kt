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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import online.partyrun.partyrunapplication.feature.match.MatchDialog
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.MatchViewModel

@Composable
fun BattleMainScreen(
    battleViewModel: BattleViewModel = hiltViewModel(),
    matchViewModel: MatchViewModel = hiltViewModel()
) {
    val matchUiState by matchViewModel.matchUiState.collectAsState()

    Content(matchViewModel, matchUiState)
}

@Composable
fun Content(
    matchViewModel: MatchViewModel,
    matchUiState: MatchUiState
) {
    if (matchUiState.isOpen) {
        MatchDialog(
            setShowDialog = {
                matchViewModel.closeMatchDialog()
            }
        )
    }

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
            connectEvent = {
                CoroutineScope(Dispatchers.IO).launch {
                    matchViewModel.connectMatchEventSource()
                }
           },
            closeEvent = {
                CoroutineScope(Dispatchers.IO).launch {
                    matchViewModel.closeMatchEventSource()
                }
            }
        )
        Text(
            text = "연결 여부: ${matchUiState.waitingEventState.isSuccess}"
        )
        Text(
            text = "메세지: ${matchUiState.waitingEventState.message}"
        )
        Spacer(modifier = Modifier.size(50.dp))
        MatchContent(
            mainTitle = "파티 생성",
            connectTitle = "파티 생성 수락",
            closeTitle = "파티 생성 거절",
            connectEvent = {
                CoroutineScope(Dispatchers.IO).launch {
                    matchViewModel.connectMatchResultEventSource()
                }
           },
            closeEvent = {
                CoroutineScope(Dispatchers.IO).launch {
                    matchViewModel.closeMatchResultEventSource()
                }
            }
        )
        Text(
            text = "상태: ${matchUiState.matchResultEventState.status.name}"
        )
        Text(
            text = "생성된 방 엔드포인트: ${matchUiState.matchResultEventState.location}"
        )
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                matchViewModel.openMatchDialog()
                matchViewModel.beginBattleMatchingProcess(
                    UserSelectedMatchDistance(
                        distance = "M1000"
                    )
                )
            }
        ) {
            Text("배틀 매칭 시작")
        }
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
    Column {
        Text(text = mainTitle)
        Spacer(modifier = Modifier.size(10.dp))
        Row {
            Button(onClick = { connectEvent() }) {
                Text(connectTitle)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { closeEvent() }) {
                Text(closeTitle)
            }
        }
    }
    Spacer(modifier = Modifier.size(20.dp))
}
