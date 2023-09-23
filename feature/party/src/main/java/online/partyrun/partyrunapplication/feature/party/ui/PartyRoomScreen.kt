package online.partyrun.partyrunapplication.feature.party.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.designsystem.component.SurfaceRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.feature.party.R
import online.partyrun.partyrunapplication.feature.party.component.PartyBackNavigationHandler
import online.partyrun.partyrunapplication.feature.party.component.PartyRoomTopAppBar
import online.partyrun.partyrunapplication.feature.party.util.copyToClipboard

@Composable
fun PartyRoomScreen(
    partyCode: String?,
    modifier: Modifier = Modifier,
    navigateToParty: () -> Unit = {},
    partyRoomViewModel: PartyRoomViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit
) {
    val partyRoomUiState by partyRoomViewModel.partyRoomUiState.collectAsState()
    val openPartyExitDialog = remember { mutableStateOf(false) }
    val partyRoomSnackbarMessage by partyRoomViewModel.snackbarMessage.collectAsState()

    Content(
        partyCode = partyCode,
        modifier = modifier,
        partyRoomUiState = partyRoomUiState,
        navigateToParty = navigateToParty,
        partyRoomViewModel = partyRoomViewModel,
        openPartyExitDialog = openPartyExitDialog,
        onShowSnackbar = onShowSnackbar,
        partyRoomSnackbarMessage = partyRoomSnackbarMessage
    )
}

@Composable
private fun Content(
    partyCode: String?,
    modifier: Modifier = Modifier,
    partyRoomUiState: PartyRoomUiState,
    navigateToParty: () -> Unit,
    partyRoomViewModel: PartyRoomViewModel,
    openPartyExitDialog: MutableState<Boolean>,
    onShowSnackbar: (String) -> Unit,
    partyRoomSnackbarMessage: String
) {
    LaunchedEffect(partyRoomSnackbarMessage) {
        if (partyRoomSnackbarMessage.isNotEmpty()) {
            onShowSnackbar(partyRoomSnackbarMessage)
            partyRoomViewModel.clearSnackbarMessage()
        }
    }

    LaunchedEffect(partyCode) {
        if (partyCode != null) {
            partyRoomViewModel.beginManagerProcess(partyCode)
        }
    }

    Box(modifier = modifier) {
        when (partyRoomUiState) {
            is PartyRoomUiState.Loading -> RoomLoadingBody()
            is PartyRoomUiState.Success ->
                RoomSuccessBody(
                    modifier = modifier,
                    partyRoomState = partyRoomUiState.partyRoomState,
                    navigateToParty = navigateToParty,
                    partyRoomViewModel = partyRoomViewModel,
                    openPartyExitDialog = openPartyExitDialog
                )

            is PartyRoomUiState.LoadFailed ->
                navigateToParty()
        }
    }
}

@Composable
fun RoomLoadingBody() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomSuccessBody(
    modifier: Modifier = Modifier,
    partyRoomState: PartyRoomState,
    navigateToParty: () -> Unit,
    partyRoomViewModel: PartyRoomViewModel,
    openPartyExitDialog: MutableState<Boolean>
) {
    // 대결 중 BackPressed 수행 시 처리할 핸들러
    PartyBackNavigationHandler(
        openPartyExitDialog = openPartyExitDialog
    ) {
        partyRoomViewModel.quitPartyRoom()
        navigateToParty()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            PartyRoomTopAppBar(
                modifier = modifier
            ) {
                partyRoomViewModel.preparingMenuMessage()
            }
        }
    ) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PartyRoomBody(
                modifier = modifier,
                partyRoomState = partyRoomState,
                openPartyExitDialog = openPartyExitDialog
            )
        }
    }
}

@Composable
private fun PartyRoomBody(
    modifier: Modifier,
    partyRoomState: PartyRoomState,
    openPartyExitDialog: MutableState<Boolean>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PartyRoomInfoBox(
            partyCode = partyRoomState.partyEvent.entryCode
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.distance),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            SurfaceRoundedRect(
                color = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                    text = partyRoomState.partyEvent.distance.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.manager),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            ManagerBox(
                partyRoomState.partyEvent.managerId
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.participants),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            ParticipantsBox(
                partyRoomState.partyEvent.participants
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuitButton(
                modifier = Modifier.weight(1f),
                openPartyExitDialog = openPartyExitDialog
            )
            Spacer(modifier = Modifier.width(10.dp))
            StartButton(
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PartyRoomInfoBox(
    partyCode: String
) {
    val context = LocalContext.current

    PartyRunGradientButton(
        onClick = {
            copyToClipboard(context, partyCode)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.party_code),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = PartyRunIcons.CopyIcon),
                    contentDescription = null
                )
            }
            Text(
                text = partyCode,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun QuitButton(
    modifier: Modifier = Modifier,
    openPartyExitDialog: MutableState<Boolean>
) {
    OutlinedButton(
        onClick = {
            openPartyExitDialog.value = true
        },
        shape = RoundedCornerShape(35.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary),
        modifier = modifier
            .shadow(5.dp, shape = CircleShape),
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = stringResource(id = R.string.quit_btn),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun StartButton(
    modifier: Modifier = Modifier
) {
    PartyRunGradientButton(
        onClick = { },
        modifier = modifier
            .shadow(5.dp, shape = CircleShape)
    ) {
        Text(
            text = stringResource(id = R.string.start_btn),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
private fun ManagerBox(
    managerId: String
) {
    SurfaceRoundedRect(
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                ) {
                    RenderAsyncUrlImage(
                        imageUrl = "https://partyrun.s3.ap-northeast-2.amazonaws.com/profile-image/partyrun-default.png",
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = managerId,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Image(
                painter = painterResource(id = R.drawable.manager),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ParticipantsBox(
    participants: List<String>
) {
    SurfaceRoundedRect(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            items(participants) { runner ->
                RunnerRow(runner = runner)
            }
        }
    }
}

@Composable
fun RunnerRow(runner: String) {
    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
        ) {
            RenderAsyncUrlImage(
                imageUrl = "https://partyrun.s3.ap-northeast-2.amazonaws.com/profile-image/partyrun-default.png",
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = runner,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
