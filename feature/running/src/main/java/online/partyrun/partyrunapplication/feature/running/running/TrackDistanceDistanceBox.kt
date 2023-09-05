package online.partyrun.partyrunapplication.feature.running.running

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.feature.running.R

@Composable
fun TrackDistanceDistanceBox(
    modifier: Modifier = Modifier,
    totalTrackDistance: Int
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = modifier
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.distance_display),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 30.dp, bottom = 5.dp),
            text = "$totalTrackDistance" + "m",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}