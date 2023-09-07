package online.partyrun.partyrunapplication.feature.running.running.component

import android.content.Context
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import online.partyrun.partyrunapplication.feature.running.R

@Composable
fun BoxWithConstraintsScope.trackRatio(context: Context): Pair<Double, Double> {
    val imageBitmap = ImageBitmap.imageResource(context.resources, R.drawable.running_track)

    // 트랙 이미지의 실제 비율
    val aspectRatio = imageBitmap.width.toDouble() / imageBitmap.height.toDouble()

    // 컴포넌트의 최대 크기와 비교하여 가로 또는 세로 크기 조절
    val trackWidth = this.maxWidth.value / aspectRatio
    val trackHeight = this.maxHeight.value / aspectRatio
    return Pair(trackWidth, trackHeight)
}
