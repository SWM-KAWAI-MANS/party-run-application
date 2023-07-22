package online.partyrun.partyrunapplication.feature.match.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunCancelDialog
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.R

@Composable
fun MatchCancelDialog(
    setShowDialog: (Boolean) -> Unit,
    matchUiState: MatchUiState,
) {
    PartyRunCancelDialog(
        onDismissRequest = {
            setShowDialog(false)
        },
        modifier = Modifier
            .width(300.dp)
            .height(300.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.canceled_matching),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(30.dp))
            CircularProgressIndicator()
        }
    }
}
