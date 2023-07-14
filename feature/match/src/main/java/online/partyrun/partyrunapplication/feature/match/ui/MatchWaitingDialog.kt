package online.partyrun.partyrunapplication.feature.match.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.ui.FormatElapsedTimer
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.R
import online.partyrun.partyrunapplication.feature.match.buildPlayerView

object Dimensions {
    val DialogHeight = 550.dp
    val DialogWidth = 300.dp
    val ExoPlayerHeight = 300.dp
    val ExoPlayerWidth = 300.dp
}

@Composable
fun MatchWaitingDialog(
    setShowDialog: (Boolean) -> Unit,
    disconnectSSE: () -> Unit,
    matchUiState: MatchUiState,
    exoPlayer: ExoPlayer,
    isBuffering: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = {
            disconnectSSE()
            setShowDialog(false)
        }
    ) {
        Surface(
            modifier = Modifier
                .width(Dimensions.DialogWidth)
                .height(Dimensions.DialogHeight),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.waiting_match_title),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .width(Dimensions.ExoPlayerWidth)
                        .height(Dimensions.ExoPlayerHeight),
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
                Spacer(modifier = Modifier.size(20.dp))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(text = stringResource(id = R.string.elapsed_time))
                    FormatElapsedTimer()
                }
                Spacer(modifier = Modifier.size(20.dp))
                IconButton(
                    onClick = {
                        disconnectSSE()
                        setShowDialog(false)
                    },
                    modifier = Modifier.size(40.dp) // IconButton의 전체 크기 조절
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black, RoundedCornerShape(50.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.cancel_button_title),
                            tint = Color.White,
                            modifier = Modifier.size(25.dp).align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}
