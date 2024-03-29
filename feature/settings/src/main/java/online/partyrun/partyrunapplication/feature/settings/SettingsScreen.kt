package online.partyrun.partyrunapplication.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.ui.SettingsTopAppBar

@Composable
fun SettingsScreen(
    onSignOut: () -> Unit = {},
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {},
    navigateToUnsubscribe: () -> Unit = {},
    onShowSnackbar: (String) -> Unit
) {
    val settingsSnackbarMessage by settingsViewModel.snackbarMessage.collectAsStateWithLifecycle()

    Content(
        onSignOut = onSignOut,
        navigateBack = navigateBack,
        navigateToUnsubscribe = navigateToUnsubscribe,
        settingsViewModel = settingsViewModel,
        onShowSnackbar = onShowSnackbar,
        settingsSnackbarMessage = settingsSnackbarMessage
    )
}

@Composable
fun Content(
    onSignOut: () -> Unit,
    settingsViewModel: SettingsViewModel,
    navigateBack: () -> Unit,
    navigateToUnsubscribe: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    settingsSnackbarMessage: String
) {
    LaunchedEffect(settingsSnackbarMessage) {
        if (settingsSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(settingsSnackbarMessage)
            settingsViewModel.clearSnackbarMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainBody(
            onSignOut = onSignOut,
            settingsViewModel = settingsViewModel,
            navigateBack = navigateBack,
            navigateToUnsubscribe = navigateToUnsubscribe
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainBody(
    onSignOut: () -> Unit,
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
            SettingsItem(
                title = stringResource(id = R.string.sign_out_title),
                action = {
                    settingsViewModel.signOut()
                    onSignOut()
                }
            )
            SettingsItem(
                title = stringResource(id = R.string.unsubscribe),
                action = navigateToUnsubscribe
            )
            ItemDivider()
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    action: () -> Unit
) {
    ItemDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        IconButton(onClick = { action() }) {
            Icon(
                painterResource(id = PartyRunIcons.ArrowForwardIos),
                contentDescription = stringResource(id = R.string.arrow_forward_desc)
            )
        }
    }
}

@Composable
private fun ItemDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .alpha(0.2f)
            .height(1.dp),
    )
}
