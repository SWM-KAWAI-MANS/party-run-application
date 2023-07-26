package online.partyrun.partyrunapplication.feature.running.battle.ready

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.LottieImage
import online.partyrun.partyrunapplication.core.ui.HeadLine
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
            .fillMaxSize(),
    ) {
        if (isConnecting) {
            ConnectingIndicator()
        } else {
            ReadyInfo()
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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LottieImage(
            modifier = Modifier.padding(top = 200.dp),
            rawAnimation = R.raw.ready
        )

        HeadLine(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = stringResource(id = R.string.comment_before_starts),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = stringResource(id = R.string.configuring_tracks),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
