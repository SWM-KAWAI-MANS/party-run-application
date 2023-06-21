package online.partyrun.partyrunapplication.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import online.partyrun.partyrunapplication.core.designsystem.theme.PartyRunApplicationTheme
import online.partyrun.partyrunapplication.core.navigation.main.BottomNavigationBar
import online.partyrun.partyrunapplication.core.navigation.main.MainNavRoutes
import online.partyrun.partyrunapplication.core.navigation.main.SetUpMainNavGraph

@Composable
fun PartyRunMain(
    onSignOut: () -> Unit
) {
    PartyRunApplicationTheme {
        /*
         Navigation Architecture Component
         메인 기준 NavHostController 인스턴스 생성
         */
        val navController = rememberNavController() // 백 스택 관리 및 현재 목적지가 어떤 Composable인지 추적

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SetUpMainGraph(
                navController = navController,
                onSignOut = onSignOut
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpMainGraph(
    navController: NavHostController,
    onSignOut: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartyRunMainPreview() {
    /*TODO*/
}
