package online.partyrun.partyrunapplication.feature.match.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientIconButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunMatchDialog
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.ui.FormatElapsedTimer
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.R
import online.partyrun.partyrunapplication.feature.match.buildPlayerView

@Composable
fun MatchWaitingDialog(
    setShowDialog: (Boolean) -> Unit,
    disconnectSSE: () -> Unit,
    matchUiState: MatchUiState,
    exoPlayer: ExoPlayer,
    isBuffering: MutableState<Boolean>
) {
    PartyRunMatchDialog(
        onDismissRequest = {
            disconnectSSE()
            setShowDialog(false)
        },
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            ExoPlayerBox(isBuffering, exoPlayer)
            Spacer(modifier = Modifier.size(20.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.size(20.dp))
            Row(
                modifier = Modifier.padding(1.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.elapsed_time),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                FormatElapsedTimer()
            }
            Spacer(modifier = Modifier.size(20.dp))

            CancelButton(disconnectSSE, setShowDialog)
        }
    }
}

@Composable
private fun ExoPlayerBox(
    isBuffering: MutableState<Boolean>,
    exoPlayer: ExoPlayer
) {
    Box(
        modifier = Modifier
            .width(250.dp)
            .height(250.dp),
    ) {
        if (isBuffering.value) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            /**
             * PlayerView를 빌드하는 과정에서 생기는 검은 로딩 화면을 보이지 않게 하기 위해
             * 미리 로딩 시킨 뒤 화면에 보여주는 Scope 추가
             */
            val scope = rememberCoroutineScope()
            val visible = remember { mutableStateOf(false) }

            AndroidView(
                factory = { it.buildPlayerView(exoPlayer) },
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (visible.value) 1f else 0.2f)
            ) {
                scope.launch {
                    delay(500L)
                    visible.value = true
                }
            }
        }
    }
}

@Composable
private fun CancelButton(
    disconnectSSE: () -> Unit,
    setShowDialog: (Boolean) -> Unit
) {
    PartyRunGradientIconButton(
        modifier = Modifier
            .size(48.dp)
            .shadow(5.dp, CircleShape),
        onClick = {
            disconnectSSE()
            setShowDialog(false)
        }
    ) {
        Icon(
            painter = painterResource(id = PartyRunIcons.Close),
            contentDescription = stringResource(id = R.string.close_button_desc)
        )
    }
}
