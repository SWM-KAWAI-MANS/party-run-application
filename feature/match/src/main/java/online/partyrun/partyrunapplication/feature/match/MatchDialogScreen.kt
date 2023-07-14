package online.partyrun.partyrunapplication.feature.match

import android.content.Context
import android.view.ViewGroup
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL
import com.google.android.exoplayer2.ui.StyledPlayerView
import online.partyrun.partyrunapplication.feature.match.ui.MatchCancelDialog
import online.partyrun.partyrunapplication.feature.match.ui.MatchDecisionDialog
import online.partyrun.partyrunapplication.feature.match.ui.MatchResultDialog
import online.partyrun.partyrunapplication.feature.match.ui.MatchWaitingDialog

@Composable
fun MatchDialog(
    setShowDialog: (Boolean) -> Unit,
    matchViewModel: MatchViewModel = viewModel(),
    exoPlayer: ExoPlayer,
    isBuffering: MutableState<Boolean>
) {
    val matchState by matchViewModel.matchUiState.collectAsStateWithLifecycle()

    when (matchState.matchProgress.name) {
        MatchProgress.WAITING.name ->
            MatchWaitingDialog(
                setShowDialog = setShowDialog,
                disconnectSSE = {
                    matchViewModel.closeMatchEventSource()
                },
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
        MatchProgress.CANCEL.name ->
            MatchCancelDialog(
                setShowDialog = setShowDialog,
                matchUiState = matchState
            )
    }
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
