package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.theme.Purple10
import online.partyrun.partyrunapplication.core.designsystem.theme.Purple40
import online.partyrun.partyrunapplication.core.designsystem.theme.White10

@Composable
fun BottomHalfOvalGradientShape(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient( // 그라디언트 적용
                    listOf(Purple10, Purple40)
                ),
                shape = RoundedCornerShape(topStartPercent = 35, topEndPercent = 35)
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun RoundedRect(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 25.dp, // 모서리 반지름 설정
    backgroundColor: Color = White10,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius) // 모서리 반지름 설정
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

