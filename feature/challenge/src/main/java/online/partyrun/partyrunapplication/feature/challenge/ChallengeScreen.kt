package online.partyrun.partyrunapplication.feature.challenge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.ui.PreparingImage

@Composable
fun ChallengeScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PreparingImage {
            Text(
                text = stringResource(id = R.string.challenge_preparing_service_comment_1),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                modifier = Modifier.padding(40.dp),
                text = stringResource(id = R.string.challenge_preparing_service_comment_2),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
