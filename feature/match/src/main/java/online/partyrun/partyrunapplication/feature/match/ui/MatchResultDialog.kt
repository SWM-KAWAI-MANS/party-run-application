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
import androidx.compose.foundation.shape.CircleShape
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
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.model.match.MatchMember
import online.partyrun.partyrunapplication.core.model.match.RunnerStatus
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
        modifier = Modifier.fillMaxSize()
    ) {
        ResultDialogContent(
            statusContent = { DisplayMatchStatusBox(matchUiState) },
            infoContent = { MatchAcceptanceInfo() }
        )
    }
}

@Composable
private fun ResultDialogContent(
    statusContent: @Composable () -> Unit,
    infoContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.size(80.dp))
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(
                    start = 30.dp,
                    end = 30.dp
                )
        ) {
            statusContent()
        }
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            infoContent()
        }
    }
}

@Composable
private fun DisplayMatchStatusBox(matchUiState: MatchUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(25.dp))
            .padding(
                top = 50.dp,
                start = 20.dp,
                end = 20.dp
            )
    ) {
        DisplayMatchStatus(matchUiState = matchUiState)
    }
}

@Composable
private fun MatchAcceptanceInfo() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = Modifier
                .width(100.dp)
                .padding(top = 5.dp, bottom = 5.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(20.dp)),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(id = R.string.completed_match),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.size(5.dp))
        Row(
            modifier = Modifier.padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                strokeWidth = 3.dp
            )
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = stringResource(id = R.string.Determining_acceptance),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.time_remaining_acceptance),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            FormatRemainingTimer(totalTime = 60)
        }
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
            // 아이디와 일치하는 RunnerInfo 찾기
            val runnerInfo = matchUiState.runnerInfoData.runners.firstOrNull { it.id == item.id }

            // 일치하는 RunnerInfo가 있으면 해당 이름과 프로필을 사용하고, 없으면 디폴트 텍스트를 사용
            val runnerName = runnerInfo?.name ?: "이름 없음"
            val runnerProfile = runnerInfo?.profile ?: ""

            MatchStatusItem(runnerName, runnerProfile, item.status)
        }
    }
}

@Composable
fun MatchStatusItem(
    runnerName: String,
    runnerProfile: String,
    runnerStatus: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
            ) {
                RenderAsyncUrlImage(
                    imageUrl = runnerProfile,
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = runnerName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        ConvertPlayerStatus(runnerStatus)
    }
}

@Composable
private fun ConvertPlayerStatus(runnerStatus: String) {
    when (runnerStatus) {
        RunnerStatus.NO_RESPONSE.name ->
            PartyRunOutlinedText(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.waiting_for_user),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        RunnerStatus.READY.name ->
            PartyRunGradientText(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.accept_matching_status),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        RunnerStatus.CANCELED.name ->
            PartyRunOutlinedText(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
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
                    status = RunnerStatus.NO_RESPONSE.name
                ),
                MatchMember(
                    id = "TEST2",
                    status = RunnerStatus.READY.name
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
