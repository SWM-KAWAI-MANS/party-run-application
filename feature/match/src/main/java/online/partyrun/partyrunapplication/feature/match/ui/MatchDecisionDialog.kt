package online.partyrun.partyrunapplication.feature.match.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunMatchDialog
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunOutlinedButton
import online.partyrun.partyrunapplication.core.ui.AnimationGaugeBar
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.R

@Composable
fun MatchDecisionDialog(
    setShowDialog: (Boolean) -> Unit,
    matchUiState: MatchUiState,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    PartyRunMatchDialog(
        onDismissRequest = {
            setShowDialog(false)
        },
        modifier = Modifier
            .width(300.dp)
            .height(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.completed_matching),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = stringResource(id = R.string.ready_to_run_desc),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(25.dp))
                AnimationGaugeBar(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp, bottom = 15.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DeclineButton { onDecline() }
                AcceptButton { onAccept() }
            }
        }
    }
}

@Composable
private fun DeclineButton(onDecline: () -> Unit) {
    PartyRunOutlinedButton(
        onClick = { onDecline() },
        shape = RoundedCornerShape(35.dp),
        borderStrokeWidth = 5.dp,
        modifier = Modifier
            .size(width = 120.dp, height = 50.dp)
            .shadow(5.dp, shape = CircleShape),
    ) {
        Text(
            text = stringResource(id = R.string.decline_button_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun AcceptButton(onAccept: () -> Unit) {
    PartyRunGradientButton(
        onClick = { onAccept() },
        modifier = Modifier
            .size(width = 120.dp, height = 50.dp)
            .shadow(5.dp, shape = CircleShape),
        contentColor = Color.White,
        containerColor = Color.Unspecified
    ) {
        Text(
            text = stringResource(id = R.string.accept_button_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
