package online.partyrun.partyrunapplication.feature.battle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.partyrun.partyrunapplication.core.designsystem.component.BottomHalfOvalGradientShape
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunMatchButton
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.ui.BackgroundBlurImage
import online.partyrun.partyrunapplication.core.ui.HeadLine
import online.partyrun.partyrunapplication.core.ui.KmInfoCard
import online.partyrun.partyrunapplication.feature.match.MatchDialog
import online.partyrun.partyrunapplication.feature.match.MatchUiState
import online.partyrun.partyrunapplication.feature.match.MatchViewModel

@Composable
fun BattleMainScreen(
    modifier: Modifier = Modifier,
    battleViewModel: BattleMainViewModel = hiltViewModel(),
    matchViewModel: MatchViewModel = hiltViewModel(),
    navigateToBattleRunning: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    val battleMainUiState by battleViewModel.battleMainUiState.collectAsState()
    val matchUiState by matchViewModel.matchUiState.collectAsState()
    val matchSnackbarMessage by matchViewModel.snackbarMessage.collectAsState()

    Content(
        modifier = modifier,
        battleMainUiState = battleMainUiState,
        matchViewModel = matchViewModel,
        navigateToBattleRunning = navigateToBattleRunning,
        matchUiState = matchUiState,
        matchSnackbarMessage = matchSnackbarMessage,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    battleMainUiState: BattleMainUiState,
    matchViewModel: MatchViewModel,
    navigateToBattleRunning: () -> Unit,
    matchUiState: MatchUiState,
    matchSnackbarMessage: String,
    onShowSnackbar: (String) -> Unit,
) {
    if (matchUiState.isAllRunnersAccepted) {
        navigateToBattleRunning()
        matchViewModel.closeMatchDialog() // 다이얼로그를 닫고 초기화 수행
    }

    LaunchedEffect(matchSnackbarMessage) {
        if (matchSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(matchSnackbarMessage)
            matchViewModel.clearSnackbarMessage()
        }
    }

    Box(modifier = modifier) {
        when (battleMainUiState) {
            is BattleMainUiState.Loading -> LoadingBody()
            is BattleMainUiState.Success -> BattleMainBody(
                matchViewModel = matchViewModel,
                matchUiState = matchUiState
            )

            is BattleMainUiState.LoadFailed -> LoadingBody()
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
fun BattleMainBody(
    matchViewModel: MatchViewModel,
    matchUiState: MatchUiState,
) {
    if (matchUiState.isOpen) {
        MatchDialog(
            setShowDialog = {
                matchViewModel.closeMatchDialog()
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundBlurImage(
            modifier = Modifier.fillMaxSize(),
            image = R.drawable.backgroundblur,
            contentAlignment = Alignment.BottomCenter
        )
        BottomHalfOvalGradientShape(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(230.dp)
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadLine(
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.battle_head_line_1),
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = stringResource(id = R.string.battle_head_line_2),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        KmInfoCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onLeftClick = { /*TODO*/ },
            onRightClick = { /*TODO*/ }
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.track_1km),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        PartyRunMatchButton(
            modifier = Modifier.padding(bottom = 70.dp),
            onClick = {
                matchViewModel.openMatchDialog()
                matchViewModel.beginBattleMatchingProcess(
                    RunningDistance(
                        distance = 1000
                    )
                )
            }
        ) {
            Text(
                text = stringResource(id = R.string.battle_matching_start),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}
