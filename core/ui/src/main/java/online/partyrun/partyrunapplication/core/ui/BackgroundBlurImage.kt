package online.partyrun.partyrunapplication.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import online.partyrun.partyrunapplication.core.designsystem.R

@Composable
fun BackgroundBlurImage(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        Image(painter = painterResource(id = R.drawable.backgroundblur), contentDescription = null)
    }
}
