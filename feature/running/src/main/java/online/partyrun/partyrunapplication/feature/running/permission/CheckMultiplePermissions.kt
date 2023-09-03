package online.partyrun.partyrunapplication.feature.running.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.shouldShowRationale
import online.partyrun.partyrunapplication.feature.running.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckMultiplePermissions(
    permissionState: MultiplePermissionsState,
    onPermissionResult: (Boolean) -> Unit,
    showPermissionDialog: MutableState<Boolean>
) {
    val context = LocalContext.current

    val permissionDescriptionProviderMap = createPermissionMap()

    when {
        permissionState.allPermissionsGranted -> {
            onPermissionResult(true)
        }

        else -> {
            onPermissionResult(false)
            RevokedPermissionsDialog(
                permissionState,
                permissionDescriptionProviderMap,
                context,
                showPermissionDialog
            )
        }
    }
}

private fun createPermissionMap() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    mapOf(
        Manifest.permission.POST_NOTIFICATIONS to NotificationPermissionDescriptionProvider(),
        Manifest.permission.ACCESS_FINE_LOCATION to LocationPermissionDescriptionProvider()
    )
} else {
    mapOf(
        Manifest.permission.ACCESS_FINE_LOCATION to LocationPermissionDescriptionProvider()
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RevokedPermissionsDialog(
    permissionState: MultiplePermissionsState,
    permissionDescriptionProviderProviderMap: Map<String, PermissionDescriptionProvider>,
    context: Context,
    showPermissionDialog: MutableState<Boolean>
) {
    /**
     * 한 번에 하나의 퍼미션만을 처리하기 위한 다이얼로그를 띄워야 하므로 for문 대신 lastOrNull() 처리
     */
    val lastRevokedPermission = permissionState.revokedPermissions.lastOrNull()

    lastRevokedPermission?.let { perm ->
        val descriptionProvider =
            permissionDescriptionProviderProviderMap[perm.permission] ?: return@let
        ShowPermissionDialog(
            permissionState,
            perm.status,
            descriptionProvider,
            context,
            showPermissionDialog
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissionDialog(
    permissionState: MultiplePermissionsState,
    permissionStatus: PermissionStatus,
    descriptionProvider: PermissionDescriptionProvider,
    context: Context,
    showPermissionDialog: MutableState<Boolean>
) {
    PermissionDialog(
        permissionDescriptionProvider = descriptionProvider,
        isPermanentlyDeclined = !permissionStatus.shouldShowRationale,
        onDismiss = { showPermissionDialog.value = false },
        onOkClick = { permissionState.launchMultiplePermissionRequest() },
        onGoToAppSettingsClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    )
}

interface PermissionDescriptionProvider {
    fun getTitle(context: Context): String
    fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String
}

class NotificationPermissionDescriptionProvider : PermissionDescriptionProvider {
    override fun getTitle(context: Context): String {
        return context.getString(R.string.notification_permission_title)
    }

    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String {
        val baseDescription = context.getString(R.string.notification_permission_description)
        return if (isPermanentlyDeclined) {
            "$baseDescription\n${context.getString(R.string.notification_permission_additional_instruction)}"
        } else {
            baseDescription
        }
    }
}

class LocationPermissionDescriptionProvider : PermissionDescriptionProvider {
    override fun getTitle(context: Context): String {
        return context.getString(R.string.location_permission_title)
    }

    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String {
        val baseDescription = context.getString(R.string.location_permission_description)
        return if (isPermanentlyDeclined) {
            "$baseDescription\n${context.getString(R.string.location_permission_additional_instruction)}"
        } else {
            baseDescription
        }
    }
}
