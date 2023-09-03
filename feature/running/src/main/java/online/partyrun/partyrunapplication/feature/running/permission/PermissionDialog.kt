package online.partyrun.partyrunapplication.feature.running.permission

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientButton
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunOutlinedButton
import online.partyrun.partyrunapplication.feature.running.R

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    permissionDescriptionProvider: PermissionDescriptionProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        confirmButton = {
            PartyRunGradientButton(
                onClick = {
                    if (isPermanentlyDeclined) {
                        onGoToAppSettingsClick()
                    } else {
                        onOkClick()
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.permission_confirm),
                )
            }
        },
        dismissButton = {
            PartyRunOutlinedButton(
                onClick = { onDismiss() }
            ) {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = stringResource(id = R.string.permission_cancel),
                )
            }
        },
        title = {
            Text(
                text = permissionDescriptionProvider.getTitle(
                    context
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        text = {
            Text(
                text = permissionDescriptionProvider.getDescription(
                    context,
                    isPermanentlyDeclined = isPermanentlyDeclined
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    )
}
