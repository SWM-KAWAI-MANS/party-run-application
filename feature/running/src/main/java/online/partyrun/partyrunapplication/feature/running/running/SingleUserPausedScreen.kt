package online.partyrun.partyrunapplication.feature.running.running

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.running.component.ControlPanelColumn
import online.partyrun.partyrunapplication.feature.running.single.SingleContentUiState
import online.partyrun.partyrunapplication.feature.running.single.SingleContentViewModel
import online.partyrun.partyrunapplication.feature.running.single.getDistanceInMeterString
import online.partyrun.partyrunapplication.feature.running.single.getRunnerRecordUiModels
import online.partyrun.partyrunapplication.feature.running_result.ui.MapWidget

@Composable
fun SingleUserPausedScreen(
    singleContentUiState: SingleContentUiState,
    singleContentViewModel: SingleContentViewModel = hiltViewModel(),
    openRunningExitDialog: MutableState<Boolean>,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 구글 맵
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        ) {
            MapWidget(
                targetDistanceFormatted = singleContentUiState.getDistanceInMeterString(),
                records = singleContentUiState.getRunnerRecordUiModels()
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.paused_logo),
                contentDescription = stringResource(id = R.string.running_paused_logo)
            )
        }
        Spacer(modifier = Modifier.size(15.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                .shadow(elevation = 5.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ControlPanelColumn(singleContentUiState, singleContentViewModel, openRunningExitDialog)
        }
    }
}
