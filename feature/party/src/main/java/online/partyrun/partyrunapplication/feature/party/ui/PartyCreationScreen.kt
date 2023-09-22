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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.RenderAsyncUrlImage
import online.partyrun.partyrunapplication.core.designsystem.component.SurfaceRoundedRect
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.feature.party.R
import online.partyrun.partyrunapplication.feature.party.component.PartyBackNavigationHandler
import online.partyrun.partyrunapplication.feature.party.component.PartyCreationTopAppBar
import online.partyrun.partyrunapplication.feature.party.util.copyToClipboard

data class User(
    val name: String,
    val imageUrl: String = "https://partyrun.s3.ap-northeast-2.amazonaws.com/profile-image/partyrun-default.png"

)

@Composable
fun PartyCreationScreen(
    modifier: Modifier = Modifier,
    navigateToParty: () -> Unit = {}
) {
    val openPartyExitDialog = remember { mutableStateOf(false) }

    Content(
        modifier = modifier,
        navigateToParty = navigateToParty,
        openPartyExitDialog = openPartyExitDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    modifier: Modifier = Modifier,
    navigateToParty: () -> Unit,
    openPartyExitDialog: MutableState<Boolean>
) {
    // 대결 중 BackPressed 수행 시 처리할 핸들러
    PartyBackNavigationHandler(
        openPartyExitDialog = openPartyExitDialog,
        navigateToParty = navigateToParty
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            PartyCreationTopAppBar(
                modifier = modifier,
                openPartyExitDialog = openPartyExitDialog
            )
        }
    ) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PartyCreationBody(
                openPartyExitDialog = openPartyExitDialog
            )
        }
    }
}

@Composable
fun PartyCreationBody(
    openPartyExitDialog: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PartyRoomInfoBox()
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
                    text = "1km",
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
            ManagerBox()
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
            ParticipantsBox()
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
private fun PartyRoomInfoBox() {
    val context = LocalContext.current

    PartyRunGradientButton(
        onClick = {
            copyToClipboard(context, "332041")
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
                    text = stringResource(id = R.string.party_number),
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
                text = "332041",
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
private fun ManagerBox() {
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
                    text = "테스트 유저",
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
fun ParticipantsBox() {
    val users = listOf(
        User("테스트 유저1"),
        User("테스트 유저2"),
        User("테스트 유저3")
    )

    SurfaceRoundedRect(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            items(users) { user ->
                UserRow(user = user)
            }
        }
    }
}

@Composable
fun UserRow(user: User) {
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
                imageUrl = user.imageUrl,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = user.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
