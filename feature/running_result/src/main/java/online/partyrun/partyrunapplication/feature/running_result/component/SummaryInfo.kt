package online.partyrun.partyrunapplication.feature.running_result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerStatusUiModel
import online.partyrun.partyrunapplication.feature.running_result.R

@Composable
fun SummaryInfo(
    runnerStatus: RunnerStatusUiModel
) {
    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.time))
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = runnerStatus.elapsedTime,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.avg_pace))
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = runnerStatus.averagePace,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.avg_altitude))
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = runnerStatus.averageAltitude.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
