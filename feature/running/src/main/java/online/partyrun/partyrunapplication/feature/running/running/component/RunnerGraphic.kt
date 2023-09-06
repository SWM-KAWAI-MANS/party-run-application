package online.partyrun.partyrunapplication.feature.running.running.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun RunnerGraphic(
    currentX: Double,
    currentY: Double,
    zIndex: Float,
    RunnerNameContent: @Composable () -> Unit,
    RunnerMarker: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .offset(
                x = currentX.dp,
                y = currentY.dp
            )
            .zIndex(zIndex) // 여기에 z-index 설정하여 user라면 최상단에 마커를 위치
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(40.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(10.dp)
            ) {
                RunnerNameContent()
            }
            RunnerMarker()
        }
    }
}
