package online.partyrun.partyrunapplication.core.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
