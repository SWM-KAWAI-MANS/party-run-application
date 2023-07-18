package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

/*
https://github.com/airbnb/lottie/blob/master/android-compose.md
 */
@Composable
fun LottieImage(
    modifier: Modifier,
    rawAnimation: Int
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(rawAnimation))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier,
    )
}
