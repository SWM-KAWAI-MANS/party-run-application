package online.partyrun.partyrunapplication.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunBackground
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.core.ui.SnackbarBox
import online.partyrun.partyrunapplication.navigation.BottomNavigationBar
import online.partyrun.partyrunapplication.navigation.SetUpMainNavGraph

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun PartyRunMain(
    appState: PartyRunAppState = rememberPartyRunAppState(),
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    onSignOut: () -> Unit
) {
    PartyRunBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        val currentDestination = appState.currentDestination?.route
        val topLevelDestinations = appState.topLevelDestinations

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    snackbarHostState,
                    snackbar = {
                        SnackbarBox(it) {
                            snackbarHostState.currentSnackbarData?.dismiss()
                        }
                    }
                )
            },
            bottomBar = {
                // 현재 목적지가 top-level일 경우에만 바텀 네비게이션 바 표시
                if (currentDestination in topLevelDestinations) {
                    BottomNavigationBar(navController = appState.navController)
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (snackbarHostState.currentSnackbarData != null) 0.3f else 1f)
                    .padding(padding)
            ) {
                SetUpMainNavGraph(
                    startDestination = MainNavRoutes.Battle.route,
                    appState = appState,
                    onSignOut = onSignOut,
                    onShowSnackbar = { message ->
                        snackbarScope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )

                if (currentDestination in topLevelDestinations) {
                    Divider( // 네비게이션바 border 상단 표현
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .align(Alignment.BottomCenter), // 스크린 바닥에 경계 표현
                        color = Color(0xFFBD55F2)
                    )
                }
            }
        }
    }
}
