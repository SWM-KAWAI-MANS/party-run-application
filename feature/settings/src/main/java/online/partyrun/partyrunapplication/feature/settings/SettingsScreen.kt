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
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunTopAppBar
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateBack: () -> Unit = {},
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SettingsTopAppBar(navigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Content(
                settingsViewModel = settingsViewModel,
                settingsUiState = settingsUiState
            )
        }
    }
}

@Composable
fun Content(
    settingsViewModel: SettingsViewModel,
    settingsUiState: SettingsUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (settingsUiState.screenState) {
            is SettingsScreenState.Unsubscribe -> UnsubscribeScreen()
            SettingsScreenState.Main -> MainBody(
                settingsViewModel = settingsViewModel
            )
        }
    }
}

@Composable
private fun MainBody(
    settingsViewModel: SettingsViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.unsubscribe),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        IconButton(onClick = { settingsViewModel.navigateToUnsubscribe() }) {
            Icon(
                painterResource(id = PartyRunIcons.ArrowForwardIos),
                contentDescription = stringResource(id = R.string.arrow_forward_desc)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopAppBar(navigateBack: () -> Unit) {
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
