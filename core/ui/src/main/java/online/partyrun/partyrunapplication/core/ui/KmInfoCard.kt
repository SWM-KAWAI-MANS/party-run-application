package online.partyrun.partyrunapplication.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        PartyRunCircularIconButton(
            onClick = onLeftClick,
        ) {
            Icon(
                painterResource(id = PartyRunIcons.ArrowBackIos), contentDescription = "null"
            )
        }
        Spacer(modifier = Modifier.width(30.dp))
        RoundedRect(
            modifier = Modifier
                .width(187.dp)
                .height(289.dp)
        ) {
            content()
        }
        Spacer(modifier = Modifier.width(30.dp))
        PartyRunCircularIconButton(
            onClick = onRightClick,
        ) {
            Icon(
                painterResource(id = PartyRunIcons.ArrowForwardIos), contentDescription = "null"
            )
        }
    }
}