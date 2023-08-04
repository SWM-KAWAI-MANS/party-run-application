package online.partyrun.partyrunapplication.feature.battle

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.gson.Gson
import online.partyrun.partyrunapplication.core.designsystem.component.BottomHalfOvalGradientShape
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunMatchButton
import online.partyrun.partyrunapplication.core.model.battle.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.ui.BackgroundBlurImage
import online.partyrun.partyrunapplication.core.ui.HeadLine
import online.partyrun.partyrunapplication.core.ui.KmInfoCard
import online.partyrun.partyrunapplication.feature.match.MatchDialog
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.MatchViewModel
import timber.log.Timber

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
    val isBuffering = remember { mutableStateOf(true) } // 로딩 상태를 관리하기 위한 MutableState

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

    if (matchUiState.isAllRunnersAccepted) {
        navigateToBattleRunningWithArgs(battleId, runnerIdsJson)
        matchViewModel.closeMatchDialog() // 다이얼로그를 닫고 초기화 수행
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
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundBlurImage(
            modifier = Modifier.fillMaxSize(),
            image = R.drawable.backgroundblur,
            contentAlignment = Alignment.BottomCenter
        )
        BottomHalfOvalGradientShape(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(230.dp)
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadLine(
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.battle_head_line_1),
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = stringResource(id = R.string.battle_head_line_2),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        KmInfoCard(
            modifier = Modifier
                .fillMaxWidth().weight(1f),
            onLeftClick = { /*TODO*/ },
            onRightClick = { /*TODO*/ }
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.track_1km),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        PartyRunMatchButton(
            modifier = Modifier.padding(bottom = 70.dp),
            onClick = {
                matchViewModel.openMatchDialog()
                matchViewModel.beginBattleMatchingProcess(
                    RunningDistance(
                        distance = 1000
                    )
                )
            }
        ) {
            Text(
                text = stringResource(id = R.string.battle_matching_start),
                style = MaterialTheme.typography.titleLarge,
            )
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

