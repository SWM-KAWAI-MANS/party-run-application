package online.partyrun.partyrunapplication.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunIconToggleButton
import online.partyrun.partyrunapplication.core.designsystem.component.SurfaceRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.Checked
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.UnChecked

@Composable
fun AgreementBox(
    modifier: Modifier = Modifier,
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
    color: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
) {
    SurfaceRoundedRect(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = border,
        color = color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 20.dp, bottom = 10.dp)
        ) {
            content()
        }
    }
}

@Composable
fun LeadingIconAgreementText(
    modifier: Modifier = Modifier,
    toggleButtonChecked: Boolean,
    toggleOnCheckedChange: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        PartyRunIconToggleButton(
            checked = toggleButtonChecked,
            onCheckedChange = {
                toggleOnCheckedChange(it)
            },
            modifier = Modifier
                .width(32.dp)
                .height(32.dp),
            icon = {
                Icon(
                    painter = painterResource(id = UnChecked),
                    contentDescription = stringResource(id = R.string.checked_icon),
                    tint = Color.Unspecified
                )
            },
            checkedIcon = {
                Icon(
                    painter = painterResource(id = Checked),
                    contentDescription = stringResource(id = R.string.unchecked_icon),
                    tint = Color.Unspecified
                )
            }
        )
        Spacer(modifier = Modifier.width(6.dp))
        content()
    }
}
