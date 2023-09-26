package online.partyrun.partyrunapplication.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun CountdownDialog(
    timeRemaining: Int,
    totalTime: Int = 5,
    size: Dp = 200.dp,
    strokeWidth: Dp = 12.dp,
    fontSize: Int = 100,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    foregroundColor: Color = Color.White,
    disabledColor: Color = Color.Gray,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Dialog(
        onDismissRequest = {}
    ) {
        val animatedProgress by animateFloatAsState(
            targetValue = timeRemaining.toFloat() / totalTime,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(backgroundColor)
        ) {
            CircularProgressIndicator(
                progress = 1f,
                modifier = Modifier.matchParentSize(),
                color = disabledColor,
                strokeWidth = strokeWidth
            )
            CircularProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier.matchParentSize(),
                color = foregroundColor,
                strokeWidth = strokeWidth
            )

            Text(
                text = "$timeRemaining",
                fontSize = fontSize.sp,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
