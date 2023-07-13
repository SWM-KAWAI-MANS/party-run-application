package online.partyrun.partyrunapplication.feature.battle

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.gson.Gson
import online.partyrun.partyrunapplication.core.model.battle.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import online.partyrun.partyrunapplication.feature.match.MatchDialog
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.MatchViewModel

@Composable
fun BattleMainScreen(
    battleViewModel: BattleMainViewModel = hiltViewModel(),
    matchViewModel: MatchViewModel = hiltViewModel(),
    navigateToBattleRunningWithArgs: (String, String) -> Unit
) {
    /* Mock Data */
    val gson = Gson()
    val runnerIds = RunnerIds(listOf("123", "456"))
    val runnerIdsJson = gson.toJson(runnerIds)
    val battleId = "64ae682dd780770fab6dca5d"

    val context = LocalContext.current
    val matchUiState by matchViewModel.matchUiState.collectAsState()
    val exoPlayer = remember { context.buildExoPlayer(getVideoUri()) }
    // 로딩 상태를 관리하기 위한 MutableState
    val isBuffering = remember { mutableStateOf(true) }

    /**
     * ExoPlayer 상태 변경 리스너 등록
     */
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_BUFFERING) {
                    isBuffering.value = true
                } else if (state == Player.STATE_READY) {
                    isBuffering.value = false
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // matchUiState.isAllRunnersAccepted
    if (matchUiState.isAllRunnersAccepted) {
        navigateToBattleRunningWithArgs(battleId, runnerIdsJson)
    }

    Content(matchViewModel, matchUiState, exoPlayer, isBuffering)
}

@Composable
fun Content(
    matchViewModel: MatchViewModel,
    matchUiState: MatchUiState,
    exoPlayer: ExoPlayer,
    isBuffering: MutableState<Boolean>
) {
    if (matchUiState.isOpen) {
        MatchDialog(
            setShowDialog = {
                matchViewModel.closeMatchDialog()
            },
            exoPlayer = exoPlayer,
            isBuffering = isBuffering
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = listOf(Color(0xFFAE2BDE), Color(0xFF5618B5)))
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            Text(stringResource(id = R.string.battle_matching_start))
        }
    }
}

private fun getVideoUri(): Uri {
    val videoUri = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    return Uri.parse(videoUri)
}

private fun Context.buildExoPlayer(uri: Uri) =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }
