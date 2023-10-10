package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import online.partyrun.partyrunapplication.core.designsystem.theme.*

private const val SHIMMER_ANIMATION_DURATION = 1000

private val DarkThemeColors = listOf(Gray20, Gray10, Gray20)
private val PartyRunThemeColors = listOf(Purple60, Purple50, Purple60)

fun Modifier.shimmerEffect(isDarkTheme: Boolean): Modifier = composed {
    val colors = if (isDarkTheme) DarkThemeColors else PartyRunThemeColors

    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(SHIMMER_ANIMATION_DURATION)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}
