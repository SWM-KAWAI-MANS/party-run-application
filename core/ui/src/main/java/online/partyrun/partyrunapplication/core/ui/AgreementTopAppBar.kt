package online.partyrun.partyrunapplication.core.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons.ArrowBackIos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementTopAppBar(
    modifier: Modifier = Modifier,
    action: () -> Unit,
    titleContent: @Composable () -> Unit,
) {
    PartyRunTopAppBar(
        modifier = modifier,
        titleContent = {
            titleContent()
        },
        navigationContent = {
            IconButton(onClick = { action() }) {
                Icon(
                    painterResource(id = ArrowBackIos),
                    contentDescription = stringResource(id = R.string.arrow_back_desc)
                )
            }
        }
    )
}
