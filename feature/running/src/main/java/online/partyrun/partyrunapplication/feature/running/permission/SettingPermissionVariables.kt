package online.partyrun.partyrunapplication.feature.running.permission

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun settingPermissionVariables(): Pair<MutableState<Boolean>, MultiplePermissionsState> {
    val showPermissionDialog = remember { mutableStateOf(false) }

    val permissionsList = listOfNotNull(
        Manifest.permission.ACCESS_FINE_LOCATION,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else null
    )
    val permissionState = rememberMultiplePermissionsState(permissions = permissionsList)
    return Pair(showPermissionDialog, permissionState)
}
