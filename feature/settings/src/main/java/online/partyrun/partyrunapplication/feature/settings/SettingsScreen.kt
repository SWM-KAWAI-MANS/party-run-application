package online.partyrun.partyrunapplication.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.ui.SettingsTopAppBar

@Composable
fun SettingsScreen(
    navigateBack: () -> Unit = {},
    navigateToUnsubscribe: () -> Unit = {},
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit
) {
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Content(
            settingsViewModel = settingsViewModel,
            settingsUiState = settingsUiState,
            navigateBack = navigateBack,
            navigateToUnsubscribe = navigateToUnsubscribe
        )
    }
}

@Composable
fun Content(
    settingsViewModel: SettingsViewModel,
    settingsUiState: SettingsUiState,
    navigateBack: () -> Unit,
    navigateToUnsubscribe: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainBody(
            settingsViewModel = settingsViewModel,
            navigateBack = navigateBack,
            navigateToUnsubscribe = navigateToUnsubscribe
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainBody(
    settingsViewModel: SettingsViewModel,
    navigateBack: () -> Unit,
    navigateToUnsubscribe: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SettingsTopAppBar(navigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.unsubscribe),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                IconButton(onClick = { navigateToUnsubscribe() }) {
                    Icon(
                        painterResource(id = PartyRunIcons.ArrowForwardIos),
                        contentDescription = stringResource(id = R.string.arrow_forward_desc)
                    )
                }
            }
        }
    }
}
