package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.partyrun.partyrunapplication.core.designsystem.theme.White10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyRunTopAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(),
    navigationContent: @Composable () -> Unit,
    titleContent: @Composable () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            titleContent()
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            navigationIconContentColor = White10,
            titleContentColor = White10
        ),
        navigationIcon = {
            navigationContent()
        }
    )
}
