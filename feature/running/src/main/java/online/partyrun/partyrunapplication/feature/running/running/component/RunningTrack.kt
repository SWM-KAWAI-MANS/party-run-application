package online.partyrun.partyrunapplication.feature.running.running.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import online.partyrun.partyrunapplication.feature.running.R

@Composable
fun RunningTrack(showArrivalFlag: Boolean) {
    val trackImage =
        if (showArrivalFlag) {
            painterResource(R.drawable.running_track_arrival)
        } else {
            painterResource(R.drawable.running_track)
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = trackImage,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
            contentDescription = stringResource(id = R.string.track_img)
        )
    }
}