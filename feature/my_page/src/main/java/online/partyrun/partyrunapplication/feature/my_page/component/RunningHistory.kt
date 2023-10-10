package online.partyrun.partyrunapplication.feature.my_page.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.LottieImage
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.feature.my_page.R
import online.partyrun.partyrunapplication.feature.my_page.RunningData

@Composable
fun RunningHistory(
    data: List<RunningData>,
    onClick: () -> Unit,
    isSingleData: Boolean
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(data) { data ->
            RunningDataCard(
                data = data,
                isSingleData = isSingleData,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun RunningDataCard(
    data: RunningData,
    isSingleData: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(start = 25.dp, end = 5.dp, top = 20.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = data.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = data.runningTime,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = data.distance,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = PartyRunIcons.ArrowForwardIos),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .offset(x = 10.dp, y = (-15).dp)
        ) {
            val icon =
                if (isSingleData) PartyRunIcons.SingleResultIcon else PartyRunIcons.BattleResultIcon

            Image(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    }
}

@Composable
fun EmptyRunningHistory() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieImage(
            modifier = Modifier.size(120.dp),
            rawAnimation = R.raw.empty_list
        )
        Text(
            text = stringResource(id = R.string.empty_list_item),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
