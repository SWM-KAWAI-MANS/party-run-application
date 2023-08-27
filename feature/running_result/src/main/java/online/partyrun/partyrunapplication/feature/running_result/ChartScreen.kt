package online.partyrun.partyrunapplication.feature.running_result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.model.running_result.ui.BattleRunnerStatusUiModel
import online.partyrun.partyrunapplication.core.ui.LineChart

enum class ChartType(val title: Int) {
    DistanceOverTime(R.string.distance),
    PacePerMinute(R.string.pace),
    AltitudeOverTime(R.string.altitude)
}

@Composable
fun ChartScreen(
    modifier: Modifier = Modifier,
    selectedRunner: BattleRunnerStatusUiModel?
) {
    var selectedChart by remember { mutableStateOf(ChartType.PacePerMinute) }

    val data = when (selectedChart) {
        ChartType.DistanceOverTime -> selectedRunner?.distanceOverTime
        ChartType.PacePerMinute -> selectedRunner?.pacePerMinute
        ChartType.AltitudeOverTime -> selectedRunner?.altitudeOvertime
    }

    Column(modifier = modifier.fillMaxSize()) {
        ChartTabBar(selectedChart) { chartType ->
            selectedChart = chartType
        }

        LineChart(data = data)
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
