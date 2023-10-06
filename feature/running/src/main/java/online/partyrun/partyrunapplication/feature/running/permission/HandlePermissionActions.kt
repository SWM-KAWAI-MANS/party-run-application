package online.partyrun.partyrunapplication.feature.running.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermissionActions(
    permissionState: MultiplePermissionsState,
    showPermissionDialog: MutableState<Boolean>
) {
    if (showPermissionDialog.value) {
        CheckMultiplePermissions(
            permissionState = permissionState,
            onPermissionResult = { if (it) showPermissionDialog.value = false },
            showPermissionDialog = showPermissionDialog
        )
    }
}
