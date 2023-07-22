package online.partyrun.partyrunapplication.feature.match.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientText
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunOutlinedText
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunResultDialog
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncImage
import online.partyrun.partyrunapplication.core.model.match.MatchMember
import online.partyrun.partyrunapplication.core.model.match.PlayerStatus
import online.partyrun.partyrunapplication.core.ui.FormatRemainingTimer
import online.partyrun.partyrunapplication.feature.match.MatchResultEventState
import online.partyrun.partyrunapplication.feature.match.MatchResultStatus
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.R

@Composable
fun MatchResultDialog(
    setShowDialog: (Boolean) -> Unit,
    matchUiState: MatchUiState
) {
    PartyRunResultDialog(
        onDismissRequest = {
            /* TODO: 취소가 불가능하게 */
            // setShowDialog(false)
        },
        modifier = Modifier
            .width(300.dp)
            .height(600.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .weight(2f),
            ) {
                DisplayMatchStatusBox(matchUiState)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MatchAcceptanceInfo()
            }
        }
    }
}

@Composable
private fun MatchAcceptanceInfo() {
    Spacer(modifier = Modifier.size(5.dp))
    Divider(
        modifier = Modifier
            .width(100.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(20.dp)),
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        modifier = Modifier.padding(10.dp),
        text = stringResource(id = R.string.completed_match),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.size(10.dp))
    Row(
        modifier = Modifier.height(50.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(30.dp),
            strokeWidth = 2.dp
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = stringResource(id = R.string.Determining_acceptance),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    Row(
        modifier = Modifier.height(50.dp)
    ) {
        Text(
            text = stringResource(id = R.string.time_remaining_acceptance),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        FormatRemainingTimer(totalTime = 60)
    }
}

@Composable
private fun DisplayMatchStatusBox(matchUiState: MatchUiState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        DisplayMatchStatus(matchUiState = matchUiState)
    }
}

@Composable
fun DisplayMatchStatus(
    matchUiState: MatchUiState
) {
    LazyColumn {
        itemsIndexed(
            matchUiState.matchResultEventState.members
        ) { index, item ->
            MatchStatusItem(player = item)
        }
    }
}

@Composable
fun MatchStatusItem(
    player: MatchMember
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RenderAsyncImage(
                image = R.drawable.mock_profile,
                size = 120,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = player.id,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        ConvertPlayerStatus(player)
    }
}

@Composable
private fun ConvertPlayerStatus(player: MatchMember) {
    when (player.status) {
        PlayerStatus.NO_RESPONSE.name ->
            PartyRunOutlinedText(
                modifier = Modifier
                    .width(68.dp)
                    .height(32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.waiting_for_user),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        PlayerStatus.READY.name ->
            PartyRunGradientText(
                modifier = Modifier
                    .width(68.dp)
                    .height(32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.accept_matching_status),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        PlayerStatus.CANCELED.name ->
            PartyRunOutlinedText(
                modifier = Modifier
                    .width(64.dp)
                    .height(32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.decline_matching_status),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Red
                )
            }
    }
}

@Preview(showBackground = true)
@Composable
fun PartyRunResultDialogPreview() {
    val testData = MatchResultEventState(
        members =
            listOf(
                MatchMember(
                    id = "TEST1",
                    status = PlayerStatus.NO_RESPONSE.name
                ),
                MatchMember(
                    id = "TEST2",
                    status = PlayerStatus.READY.name
                )
            ),
        status = MatchResultStatus.WAIT
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MatchResultDialog(
            setShowDialog = {},
            matchUiState = MatchUiState(
                matchResultEventState = testData
            )
        )
    }
}
