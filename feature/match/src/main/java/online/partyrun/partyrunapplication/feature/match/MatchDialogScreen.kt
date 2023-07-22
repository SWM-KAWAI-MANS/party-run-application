package online.partyrun.partyrunapplication.feature.match

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
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
                    matchViewModel.disconnectMatchEventSource()
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
