package online.partyrun.partyrunapplication.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.LottieImage

@Composable
fun PreparingImage(
    comment: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HeadLine(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .align(Alignment.TopCenter)
        ) {
            comment()
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            LottieImage(
                modifier = Modifier.size(250.dp).padding(bottom = 70.dp),
                rawAnimation = R.raw.preparing_service
            )
        }
    }
}
