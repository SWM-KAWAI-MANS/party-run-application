package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import online.partyrun.partyrunapplication.core.designsystem.theme.White10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyRunTopAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    containerColor: Color = MaterialTheme.colorScheme.background,
    navigationContent: @Composable () -> Unit,
    actionsContent: @Composable () -> Unit = {},
    titleContent: @Composable () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            titleContent()
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = White10,
            titleContentColor = White10,
            actionIconContentColor = White10
        ),
        navigationIcon = {
            navigationContent()
        },
        actions = {
            actionsContent()
        }
    )
}
