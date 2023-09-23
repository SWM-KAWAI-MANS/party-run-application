package online.partyrun.partyrunapplication.feature.party.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.feature.party.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyRoomTopAppBar(
    modifier: Modifier,
    onClick: () -> Unit
) {
    PartyRunTopAppBar(
        modifier = modifier,
        actionsContent = {
            IconButton(
                onClick = {
                    onClick()
                }
            ) {
                Icon(
                    painterResource(id = PartyRunIcons.Menu),
                    contentDescription = stringResource(id = R.string.ic_menu_desc)
                )
            }
        },
        titleContent = {
            Text(
                text = stringResource(id = R.string.party_room)
            )
        }
    )
}