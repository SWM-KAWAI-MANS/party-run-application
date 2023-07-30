package online.partyrun.partyrunapplication.feature.running.battle.running

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.partyrun.partyrunapplication.core.model.battle.BattleStatus
import online.partyrun.partyrunapplication.core.model.battle.RunnerStatus

@Composable
fun BattleRunningScreen(
    battleState: BattleStatus,
) {
    Spacer(modifier = Modifier.size(30.dp))
    LazyColumn {
        items(battleState.battleInfo) { runnerData ->
            RealtimeBattleScreenItem(runner = runnerData)
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
fun RealtimeBattleScreenItem(runner: RunnerStatus) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(15.dp)
    ) {
        RunnerStatsColumn(runner)
    }
}

@Composable
fun RunnerStatsColumn(runner: RunnerStatus) {
    Column {
        Text(text = "Runner: ${runner.runnerName}")
        Text(
            text = "Distance: ${runner.distance} KM",
            fontSize = 20.sp
        )
        Text(text = "Rank: ${runner.currentRank}")
        Text(text = "Current Round: ${runner.currentRound}")
    }
}
