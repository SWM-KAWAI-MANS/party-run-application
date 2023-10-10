package online.partyrun.partyrunapplication.feature.my_page.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.shimmerEffect

@Composable
fun StatusElement(
    value: String,
    title: String,
    statusElementImage: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.height(80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statusElementImage()
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun ShimmerStatusElement() {
    Column(
        modifier = Modifier.height(80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .shimmerEffect(),
        )
        Spacer(
            modifier = Modifier
                .height(5.dp)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(24.dp)
                .shimmerEffect(),
        )
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(20.dp)
                .shimmerEffect(),
        )
    }
}