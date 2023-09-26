package online.partyrun.partyrunapplication.feature.running.ready

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun SingleReadyScreen(

) {
    ReadyScreenContent()
}

@Composable
fun ReadyScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        SingleReadyInfo()
    }
}

@Composable
fun SingleReadyInfo() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HeadLine(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = stringResource(id = R.string.single_comment_before_starts),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = stringResource(id = R.string.configuring_robot),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        LottieImage(
            modifier = Modifier.padding(top = 150.dp),
            rawAnimation = R.raw.single_ready
        )
    }
}