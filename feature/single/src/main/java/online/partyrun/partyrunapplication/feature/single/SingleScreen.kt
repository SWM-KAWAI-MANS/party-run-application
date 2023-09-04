package online.partyrun.partyrunapplication.feature.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunCircularIconButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.SurfaceRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.ui.HeadLine

@Composable
fun SingleScreen(
    modifier: Modifier = Modifier,
    singleViewModel: SingleViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit
) {
    val singleUiState by singleViewModel.singleUiState.collectAsStateWithLifecycle()
    val singleSnackbarMessage by singleViewModel.snackbarMessage.collectAsStateWithLifecycle()
    val targetDistance by singleViewModel.targetDistance.collectAsStateWithLifecycle()
    val targetTime by singleViewModel.targetTime.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        singleUiState = singleUiState,
        singleViewModel = singleViewModel,
        targetDistance = targetDistance,
        targetTime = targetTime,
        singleSnackbarMessage = singleSnackbarMessage,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    singleUiState: SingleUiState,
    singleViewModel: SingleViewModel,
    targetDistance: Int,
    targetTime: Int,
    singleSnackbarMessage: String,
    onShowSnackbar: (String) -> Unit
) {

    LaunchedEffect(singleSnackbarMessage) {
        if (singleSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(singleSnackbarMessage)
            singleViewModel.clearSnackbarMessage()
        }
    }

    Box(modifier = modifier) {
        when (singleUiState) {
            is SingleUiState.Loading -> LoadingBody()
            is SingleUiState.Success -> SingleMainBody(
                singleViewModel = singleViewModel,
                targetDistance = targetDistance,
                targetTime = targetTime
            )

            is SingleUiState.LoadFailed -> LoadingBody()
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
fun SingleMainBody(
    singleViewModel: SingleViewModel,
    targetDistance: Int,
    targetTime: Int
) {
    SingleContent(
        singleViewModel = singleViewModel,
        targetDistance = targetDistance,
        targetTime = targetTime
    )
}

@Composable
private fun SingleContent(
    singleViewModel: SingleViewModel,
    targetDistance: Int,
    targetTime: Int
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadLine(
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.single_headline_1),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = stringResource(id = R.string.single_headline_2),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        SurfaceRoundedRect(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TargetDistanceSetting(
                        singleViewModel = singleViewModel,
                        targetDistance = targetDistance
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TargetTimeSetting(
                        singleViewModel = singleViewModel,
                        targetTime = targetTime
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PartyRunGradientButton(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = stringResource(R.string.single_start)
                )
            }
        }
    }
}

@Composable
private fun TargetDistanceSetting(
    singleViewModel: SingleViewModel,
    targetDistance: Int
) {
    val displayTargetDistance =
        String.format("%04.2f", (targetDistance.toFloat() / 1000f) * 100f / 100)
    TargetSettingRow(
        title = "목표 거리",
        displayValue = displayTargetDistance,
        unit = "KM",
        onIncrement = { singleViewModel.incrementTargetDistance() },
        onDecrement = { singleViewModel.decrementTargetDistance() }
    )
}

@Composable
private fun TargetTimeSetting(singleViewModel: SingleViewModel, targetTime: Int) {
    val hours = targetTime / 60
    val minutes = targetTime % 60
    val displayTargetTime = String.format("%02d.%02d", hours, minutes)

    TargetSettingRow(
        title = "목표 시간",
        displayValue = displayTargetTime,
        unit = "분",
        onIncrement = { singleViewModel.incrementTargetTime() },
        onDecrement = { singleViewModel.decrementTargetTime() }
    )
}

@Composable
private fun TargetSettingRow(
    modifier: Modifier = Modifier,
    title: String,
    displayValue: String,
    unit: String,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PartyRunCircularIconButton(
            modifier = Modifier.size(45.dp),
            onClick = onDecrement,
        ) {
            Icon(
                painterResource(id = PartyRunIcons.Remove),
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier
        ) {
            Text(
                text = displayValue,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(bottom = 10.dp),
                text = unit,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        PartyRunCircularIconButton(
            modifier = Modifier.size(45.dp),
            onClick = onIncrement,
        ) {
            Icon(
                painterResource(id = PartyRunIcons.Add),
                contentDescription = null
            )
        }
    }
}
