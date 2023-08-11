package online.partyrun.partyrunapplication.feature.running_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientText
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult
import online.partyrun.partyrunapplication.core.model.running_result.BattleRunnerStatus

data class User(val imageRes: Int, val name: String)

val mockUsers = listOf(
    User(R.drawable.mock_profile2, "가나다"),
    User(R.drawable.mock_profile2, "가나다라"),
)

@Composable
fun RunningResultScreen(
    modifier: Modifier = Modifier,
    runningResultViewModel: RunningResultViewModel = hiltViewModel()
) {
    val runningResultUiState by runningResultViewModel.runningResultUiState.collectAsStateWithLifecycle()

    Content(
        runningResultUiState = runningResultUiState
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    runningResultUiState: RunningResultUiState
) {
    Box(modifier = modifier) {
        when (runningResultUiState) {
            is RunningResultUiState.Loading -> LoadingBody()
            is RunningResultUiState.Success -> RunningResultBody(battleResult = runningResultUiState.battleResult)
            is RunningResultUiState.LoadFailed -> LoadingBody()
        }
    }
}

@Composable
private fun LoadingBody() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}


@Composable
private fun RunningResultBody(
    battleResult: BattleResult
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray) // /*TODO: 색상 임시 */
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Image(
                    painter = painterResource(id = PartyRunIcons.DistanceIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .padding(top = 3.dp),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                )
                Text(
                    text = battleResult.targetDistanceFormatted, // "X,xxx m"로 형식화
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStartPercent = 15, topEndPercent = 15)
                )
                .align(Alignment.BottomCenter),
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(color = Color.Transparent)
                        .offset(y = (-40).dp)
                ) {
                    UserProfiles(
                        users = mockUsers,
                        battleResult = battleResult
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
                    ) {
                        Text(text = battleResult.battleDate)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "${stringResource(id = R.string.battle_title)} ${battleResult.targetDistanceInKm}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(id = R.string.avg_pace))
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "임시",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Column(
                            modifier = Modifier.padding(5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(id = R.string.time))
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "임시",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Column(
                            modifier = Modifier.padding(5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(id = R.string.calories))
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "임시",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfiles(
    users: List<User>,
    battleResult: BattleResult
) {
    LazyRow(
        modifier = Modifier.padding(all = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(users) { index, user ->
            UserProfile(
                user = user,
                battleRunnerStatus = battleResult.battleRunnerStatus?.get(index) ?: BattleRunnerStatus()
            )
        }
    }
}

@Composable
fun UserProfile(
    user: User,
    battleRunnerStatus: BattleRunnerStatus
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(id = user.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column() {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = battleRunnerStatus.elapsedTime,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 10.dp, y = (-22).dp)
        ) {
            PartyRunGradientText(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${battleRunnerStatus.rank} ${stringResource(id = R.string.rank)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RunningResultScreenPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        RunningResultScreen()
    }
}
