package online.partyrun.partyrunapplication.feature.match

import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.feature.match.ui.MatchDecisionDialog
import online.partyrun.partyrunapplication.feature.match.ui.MatchResultDialog
import online.partyrun.partyrunapplication.feature.match.ui.MatchWaitingDialog

@Composable
fun MatchDialog(
    setShowDialog: (Boolean) -> Unit,
    matchViewModel: MatchViewModel = viewModel(),
) {
    val context = LocalContext.current
    val matchState by matchViewModel.matchUiState.collectAsStateWithLifecycle()
    val exoPlayer = remember { context.buildExoPlayer(getVideoUri()) }
    // 로딩 상태를 관리하기 위한 MutableState
    val isBuffering = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(6000L)
            matchViewModel.cycleMatchProgressType() // ViewModel에 정의된 상태 업데이트 함수
        }
    }

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
                matchUiState = matchState,
                exoPlayer = exoPlayer,
                isBuffering = isBuffering
            )
        MatchProgress.DECISION.name ->
            MatchDecisionDialog(
                setShowDialog = setShowDialog,
                matchUiState = matchState,
                onAccept = {
                    matchViewModel.onUserDecision(true)
                },
                onDecline = {
                    matchViewModel.onUserDecision(false)
                }
            )
        MatchProgress.RESULT.name ->
            MatchResultDialog(
                setShowDialog = setShowDialog,
                matchUiState = matchState,
            )
    }
}

private fun getVideoUri(): Uri {
    val videoUri = "https://cdn.pixabay.com/vimeo/462182247/trning-50884.mp4?width=3840&hash=b62785592f7d9ca46a0af26e9883996278ac2483"
    return Uri.parse(videoUri)
}

private fun Context.buildExoPlayer(uri: Uri) =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode = Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }

fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
    StyledPlayerView(this).apply {
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        useController = false
        resizeMode = RESIZE_MODE_ZOOM
        setBackgroundColor(Color.Transparent.hashCode()) // 배경을 투명하게 설정
    }

@Composable
fun AnimationGaugeBar(
    color: Color // 게이지 색
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val curValue = animateFloatAsState(
        targetValue = if(animationPlayed) 0f else 100f,
        animationSpec = tween(
            durationMillis = 10000, // 애니메이션 지속 시간 1초
            delayMillis = 0, // 애니메이션이 시작하기 전 대기 시간 없음
            easing = LinearEasing // 일정한 속도로 애니메이션 진행
        )
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curValue.value / 100f)
                .clip(CircleShape)
                .background(color = color)
                .padding(8.dp)
        )
    }
}
