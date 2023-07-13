package online.partyrun.partyrunapplication.feature.running.battle.ready

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import online.partyrun.partyrunapplication.feature.running.R

@Composable
fun BattleReadyScreen(
    isConnecting: Boolean
) {
    ReadyScreenContent(isConnecting = isConnecting)
}

@Composable
fun ReadyScreenContent(isConnecting: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            if (isConnecting) {
                ConnectingIndicator()
            } else {
                ReadyInfo()
            }
        }
    }
}

@Composable
fun ConnectingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ReadyInfo() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.comment_before_starts),
            fontSize = 20.sp
        )
        Text(
            text = stringResource(id = R.string.configuring_tracks),
            fontSize = 20.sp
        )
    }
}
