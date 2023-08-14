package online.partyrun.partyrunapplication.core.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(navigateBack: () -> Unit) {
    PartyRunTopAppBar(
        modifier = Modifier,
        titleContent = {
            Text(stringResource(id = R.string.settings))
        },
        navigationContent = {
            IconButton(
                onClick = {
                    navigateBack()
                }
            ) {
                Icon(
                    painterResource(id = PartyRunIcons.ArrowBackIos),
                    contentDescription = stringResource(id = R.string.arrow_back_desc)
                )
            }
        }
    )
}
