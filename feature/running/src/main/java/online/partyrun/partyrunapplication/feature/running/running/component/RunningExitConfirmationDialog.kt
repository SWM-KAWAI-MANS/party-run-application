package online.partyrun.partyrunapplication.feature.running.running.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunOutlinedButton
import online.partyrun.partyrunapplication.feature.running.R

@Composable
fun RunningExitConfirmationDialog(
    openRunningExitDialog: MutableState<Boolean>,
    exitMessage: @Composable () -> Unit,
    confirmExit: () -> Unit
) {
    if (openRunningExitDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openRunningExitDialog.value = false
            },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.exit_dialog_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    exitMessage()
                }
            },
            confirmButton = {
                PartyRunGradientButton(
                    onClick = {
                        openRunningExitDialog.value = false
                        confirmExit()
                    },
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp)
                        .shadow(5.dp, shape = CircleShape)
                ) {
                    Text(
                        text = stringResource(id = R.string.exit_dialog_confirm),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                PartyRunOutlinedButton(
                    onClick = {
                        openRunningExitDialog.value = false
                    },
                    shape = RoundedCornerShape(35.dp),
                    borderStrokeWidth = 5.dp,
                    modifier = Modifier
                        .height(50.dp)
                        .shadow(5.dp, shape = CircleShape),
                ) {
                    Text(
                        text = stringResource(id = R.string.exit_dialog_cancel),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )
    }
}
