package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.theme.Purple10
import online.partyrun.partyrunapplication.core.designsystem.theme.Purple70
import online.partyrun.partyrunapplication.core.designsystem.theme.White10

@Composable
fun PartyRunOutlinedText(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    borderStrokeWidth: Dp = 3.dp,
    borderStrokeColor: Color = MaterialTheme.colorScheme.outline,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        border = BorderStroke(
            width = borderStrokeWidth,
            color = borderStrokeColor,
        ),
        color = White10,
        shadowElevation = 3.dp,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun PartyRunGradientText(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .background(
                brush = Brush.linearGradient( // 그라디언트 적용
                    listOf(Purple70, Purple10)
                ),
                shape = RoundedCornerShape(35.dp)
            ),
        color = Color.Transparent,
        contentColor = White10,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
