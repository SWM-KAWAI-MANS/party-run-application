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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import online.partyrun.partyrunapplication.core.navigation.main.BottomNavigationBar
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.core.navigation.main.SetUpMainNavGraph

@Composable
fun PartyRunMain(
    onSignOut: () -> Unit
) {
    /*
     Navigation Architecture Component
     메인 기준 NavHostController 인스턴스 생성
     */
    val navController = rememberNavController() // 백 스택 관리 및 현재 목적지가 어떤 Composable인지 추적

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        SetUpMainGraph(
            navController = navController,
            onSignOut = onSignOut
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpMainGraph(
    navController: NavHostController,
    onSignOut: () -> Unit
) {
    /**
     * 현재 destination 가져와 arguments는 고려하지 않고 route만 비교 -> currentRoute
     */
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route?.substringBefore("?")
    val hiddenRoutes = setOf(MainNavRoutes.BattleRunning.route, MainNavRoutes.Settings.route)

    Scaffold(
        bottomBar = {
            if (currentRoute !in hiddenRoutes) {
                BottomNavigationBar(navController = navController)
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
                navController = navController,
                onSignOut = onSignOut
            )

            if (currentRoute !in hiddenRoutes) {
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
