package online.partyrun.partyrunapplication.feature.running.running.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.feature.running.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunningTopAppBar(
    openRunningExitDialog: MutableState<Boolean>
) {
    PartyRunTopAppBar(
        modifier = Modifier,
        navigateToContent = {
            IconButton(onClick = { openRunningExitDialog.value = true }) {
                Icon(
                    painterResource(id = PartyRunIcons.ArrowBackIos),
                    contentDescription = stringResource(id = R.string.arrow_back_desc)
                )
            }
        },
        actionsContent = {
            IconButton(onClick = { }) {
                Icon(
                    painterResource(id = PartyRunIcons.Menu),
                    contentDescription = stringResource(id = R.string.menu_desc)
                )
            }
        }
    )
}