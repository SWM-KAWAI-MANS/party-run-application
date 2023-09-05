package online.partyrun.partyrunapplication.feature.running.running.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.model.battle.RunnerStatus
import online.partyrun.partyrunapplication.feature.running.R

@Composable
fun RunnerMarker(runner: RunnerStatus) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        // 마커 프레임
        Image(
            modifier = Modifier.size(70.dp),
            painter = painterResource(id = R.drawable.runner_img_marker),
            contentDescription = stringResource(id = R.string.runner_img_marker)
        )
        // 유저 프로필 이미지
        Box(
            modifier = Modifier
                .size(42.dp)
                .offset(y = (-6).dp)
                .clip(CircleShape)
        ) {
            RenderAsyncUrlImage(
                imageUrl = runner.runnerProfile,
                contentDescription = null
            )
        }
    }
}