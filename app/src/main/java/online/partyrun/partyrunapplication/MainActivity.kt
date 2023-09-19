package online.partyrun.partyrunapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.ui.PartyRunMain
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.feature.battle.BattleViewModel
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val battleViewModel: BattleViewModel by viewModels()

    private lateinit var appUpdateManager: AppUpdateManager

    // 업데이트 타입 변경 가능 -> 현재 IMMEDIATE
    private val updateType = AppUpdateType.IMMEDIATE

    /**
     * 사용자로부터 권한을 요청하고 그 결과를 받아 처리하기 위한 코드
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) { // 모든 권한이 승인되었을 때
                Timber.tag("MainActivity").d("권한 승인")
            } else {
                Timber.tag("MainActivity").d("일부 권한이 거부되었습니다.")
                // 거부된 권한에 대한 처리
            }
        }


    /**
     * 사용자로부터 권한이 부여되어 있는지 확인하고, 부여되어 있지 않다면 권한을 요청
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askPermissions() {
        val hasLocationPermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasForegroundServicePermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)

        val permissionsToRequest = mutableListOf<String>()
        if (!hasForegroundServicePermission) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (!hasLocationPermission) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 최신 업데이트 정보 확인
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        checkForAppUpdates()

        battleViewModel.terminateOngoingBattle() // 앱이 처음 시작될 때 진행 중인 배틀이 있다면 종료 요청
        askPermissions() // 권한 확인 및 요청

        setContent {
            PartyRunApplicationTheme(
                darkTheme = isSystemInDarkTheme(),
                androidTheme = false,
                disableDynamicTheming = true,
            ) {
                PartyRunMain(
                    onSignOut = {
                        lifecycleScope.launch {
                            performSignOutProcess()
                        }
                    }
                )
            }
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            when (result.resultCode) {
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED ->
                    Toast.makeText(
                        applicationContext,
                        applicationContext.getString(R.string.result_in_app_update_failed),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private fun checkForAppUpdates() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(updateType)
                        .build()
                )
            }
        }
    }

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            val msg = applicationContext.getString(R.string.update_download_success)
            Toast.makeText(
                applicationContext,
                msg,
                Toast.LENGTH_LONG
            ).show()
            lifecycleScope.launch {
                delay(3000)
                appUpdateManager.completeUpdate()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (updateType == AppUpdateType.IMMEDIATE) {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo

            appUpdateInfoTask.addOnSuccessListener { info ->
                val isUpdateAvailable =
                    info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                if (isUpdateAvailable) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                            .build()
                    )
                }
            }
        }
    }

    private fun performSignOutProcess() {
        setIntentActivity(AuthActivity::class.java)
        overridePendingTransition(0, 0) // 전환 애니메이션 생략
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }
}
