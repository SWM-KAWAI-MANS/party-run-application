package online.partyrun.partyrunapplication.feature.party

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
fun PartyScreen(

) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PreparingImage {
            Text(
                text = "파티 서비스 준비 중",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                modifier = Modifier.padding(40.dp),
                text = "함께 뛰는 파티 서비스",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
