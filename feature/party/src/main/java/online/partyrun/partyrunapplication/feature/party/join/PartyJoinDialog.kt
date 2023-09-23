package online.partyrun.partyrunapplication.feature.party.join

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunDefaultDialog
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.addFocusCleaner
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyDialogInfoTextColor
import online.partyrun.partyrunapplication.feature.party.PartyViewModel
import online.partyrun.partyrunapplication.feature.party.R
import online.partyrun.partyrunapplication.feature.party.component.PartyCodeTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PartyJoinDialog(
    modifier: Modifier = Modifier,
    partyViewModel: PartyViewModel,
    onDismissRequest: () -> Unit,
    navigateToPartyRoom: (String, Boolean) -> Unit
) {
    val partyCodeInput by partyViewModel.partyCodeInput.collectAsStateWithLifecycle()

    PartyRunDefaultDialog(
        onDismissRequest = { },
        cornerRadius = 10.dp,
        strokeWidth = 2.dp,
        strokeColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(15.dp)
                .addFocusCleaner(
                    keyboardController = keyboardController!!,
                    focusManager = focusManager
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        partyViewModel.clearPartyCodeInput()
                        onDismissRequest()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = PartyRunIcons.Close),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text(
                text = stringResource(id = R.string.party_dialog_info),
                style = MaterialTheme.typography.titleSmall,
                color = PartyDialogInfoTextColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PartyCodeTextField(
                    keyboardController = keyboardController,
                    focusManager = focusManager,
                    text = partyCodeInput,
                    onTextChanged = { newCode ->
                        partyViewModel.setPartyCodeInput(newCode)
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            PartyRunGradientButton(
                onClick = {
                    onDismissRequest()
                    navigateToPartyRoom(partyCodeInput, false) // 참여자 -> 매니저 권한 미부여 == false
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 7.dp),
                    text = stringResource(id = R.string.party_dialog_join),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}
