package online.partyrun.partyrunapplication.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunCircularIconButton
import online.partyrun.partyrunapplication.core.designsystem.component.RoundedRect
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons

@Composable
fun KmInfoCard(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        PartyRunCircularIconButton(
            modifier = Modifier.size(45.dp),
            onClick = onLeftClick,
        ) {
            Icon(
                painterResource(id = PartyRunIcons.ArrowBackIos),
                contentDescription = stringResource(id = R.string.arrow_back_desc)
            )
        }
        RoundedRect(
            modifier = Modifier
                .width(187.dp)
                .height(289.dp)
        ) {
            content()
        }
        PartyRunCircularIconButton(
            modifier = Modifier.size(45.dp),
            onClick = onRightClick,
        ) {
            Icon(
                painterResource(id = PartyRunIcons.ArrowForwardIos),
                contentDescription = stringResource(id = R.string.arrow_forward_desc)
            )
        }
    }
}
