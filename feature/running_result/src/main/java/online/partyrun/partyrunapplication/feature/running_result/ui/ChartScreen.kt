package online.partyrun.partyrunapplication.feature.running_result.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerStatusUiModel
import online.partyrun.partyrunapplication.core.ui.LineChart
import online.partyrun.partyrunapplication.feature.running_result.R

enum class ChartType(val title: Int) {
    DistanceOverTime(R.string.distance),
    PacePerMinute(R.string.pace),
    AltitudeOverTime(R.string.altitude)
}

@Composable
fun ChartScreen(
    modifier: Modifier = Modifier,
    runnerStatus: RunnerStatusUiModel?
) {
    var selectedChart by remember { mutableStateOf(ChartType.DistanceOverTime) }

    // selectedChart 값에 따라 reverseYAxis 값을 설정
    val reverseYAxis = selectedChart == ChartType.PacePerMinute

    val data = when (selectedChart) {
        ChartType.DistanceOverTime -> runnerStatus?.distanceOverTime
        ChartType.PacePerMinute -> runnerStatus?.pacePerMinute
        ChartType.AltitudeOverTime -> runnerStatus?.altitudeOvertime
    }

    Column(modifier = modifier.fillMaxSize()) {
        ChartTabBar(selectedChart) { chartType ->
            selectedChart = chartType
        }

        LineChart(
            data = data,
            reverseYAxis = reverseYAxis
        )
    }
}

@Composable
fun ChartTabBar(selectedChart: ChartType, onTabSelected: (ChartType) -> Unit) {
    TabRow(
        modifier = Modifier.padding(vertical = 10.dp),
        selectedTabIndex = selectedChart.ordinal
    ) {
        ChartType.values().forEach { chartType ->
            Tab(
                modifier = Modifier.padding(10.dp),
                selected = selectedChart == chartType,
                onClick = { onTabSelected(chartType) }
            ) {
                Text(
                    text = stringResource(id = chartType.title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
