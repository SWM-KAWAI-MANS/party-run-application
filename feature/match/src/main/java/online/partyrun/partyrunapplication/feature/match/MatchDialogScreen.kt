package online.partyrun.partyrunapplication.feature.match

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL
import com.google.android.exoplayer2.ui.StyledPlayerView
import online.partyrun.partyrunapplication.feature.match.ui.MatchCancelDialog
import online.partyrun.partyrunapplication.feature.match.ui.MatchDecisionDialog
import online.partyrun.partyrunapplication.feature.match.ui.MatchResultDialog
import online.partyrun.partyrunapplication.feature.match.ui.MatchWaitingDialog
import online.partyrun.partyrunapplication.feature.match.util.ExoPlayerCache
import online.partyrun.partyrunapplication.feature.match.util.getCacheDataSourceFactory

@Composable
fun MatchDialog(
    setShowDialog: (Boolean) -> Unit,
    matchViewModel: MatchViewModel = viewModel(),
) {
    val context = LocalContext.current
    val matchState by matchViewModel.matchUiState.collectAsStateWithLifecycle()
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

    when (matchState.matchProgress.name) {
        MatchProgress.WAITING.name ->
            MatchWaitingDialog(
                setShowDialog = setShowDialog,
                disconnectMatching = {
                    matchViewModel.cancelMatchWaitingEvent()
                },
                exoPlayer = exoPlayer,
                isBuffering = isBuffering
            )
        MatchProgress.DECISION.name ->
            MatchDecisionDialog(
                setShowDialog = setShowDialog,
                onAccept = {
                    matchViewModel.onUserDecision(true)
                },
                onDecline = {
                    matchViewModel.onUserDecision(false)
                }
            )
        MatchProgress.RESULT.name ->
            MatchResultDialog(
                matchUiState = matchState
            )
        MatchProgress.CANCEL.name ->
            MatchCancelDialog(
                setShowDialog = setShowDialog
            )
    }
}

private fun getVideoUri(): Uri {
    val videoUri = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    return Uri.parse(videoUri)
}

private fun Context.buildExoPlayer(uri: Uri): ExoPlayer {
    val cache = ExoPlayerCache.get(this)
    val dataSourceFactory = getCacheDataSourceFactory(cache)
    val mediaItem = MediaItem.Builder()
        .setUri(uri)
        .build()
    // Cache를 사용하는 데이터 소스 팩토리를 사용하여 MediaSource 생성
    val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
        .createMediaSource(mediaItem)
    return ExoPlayer.Builder(this).build().apply {
        setMediaSource(mediaSource)
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }
}

fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    StyledPlayerView(this).apply {
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        useController = false
        resizeMode = RESIZE_MODE_FILL
        setBackgroundColor(Color.Transparent.hashCode()) // 배경을 투명하게 설정
    }

