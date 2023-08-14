package online.partyrun.partyrunapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.ui.PartyRunMain
import online.partyrun.partyrunapplication.core.common.extension.setIntentActivity
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.feature.battle.BattleMainViewModel
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val battleViewModel: BattleMainViewModel by viewModels()

    /**
     * 사용자로부터 위치 접근 권한을 요청하고 그 결과를 받아 처리하기 위한 코드
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) { // 사용자가 권한을 승인하면
                Timber.tag("MainActivity").d("위치 접근 권한 승인")
            }
        }

    /**
     * 사용자로부터 위치 접근 권한이 부여되어 있는지 확인하고, 부여되어 있지 않다면 권한을 요청
     */
    private fun askPermissions() {
        val hasLocationPermission = PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (!hasLocationPermission) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun performSignOutProcess() {
        setIntentActivity(AuthActivity::class.java)
        overridePendingTransition(0, 0) // 전환 애니메이션 생략
        finish()
    }
}
