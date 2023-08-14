package online.partyrun.partyrunapplication.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunBackground
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.navigation.BottomNavigationBar
import online.partyrun.partyrunapplication.navigation.SetUpMainNavGraph

@Composable
fun PartyRunMain(
    appState: PartyRunAppState = rememberPartyRunAppState(),
    onSignOut: () -> Unit
) {
    PartyRunBackground {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            SetUpMainGraph(
                appState = appState,
                onSignOut = onSignOut
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpMainGraph(
    appState: PartyRunAppState,
    onSignOut: () -> Unit
) {
    val currentDestination = appState.currentDestination?.route
    val topLevelDestinations = appState.topLevelDestinations

    Scaffold(
        bottomBar = {
            // 현재 목적지가 top-level일 경우에만 바텀 네비게이션 바 표시
            if (currentDestination in topLevelDestinations) {
                BottomNavigationBar(navController = appState.navController)
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            SetUpMainNavGraph(
                startDestination = MainNavRoutes.Battle.route,
                appState = appState,
                onSignOut = onSignOut
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

@Preview(showBackground = true)
@Composable
fun PartyRunMainPreview() {
    /*TODO*/
}
