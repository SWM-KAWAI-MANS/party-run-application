package online.partyrun.partyrunapplication.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import online.partyrun.partyrunapplication.navigation.BottomNavigationBar
import online.partyrun.partyrunapplication.navigation.NavRoutes
import online.partyrun.partyrunapplication.navigation.SetUpNavGraph
import online.partyrun.partyrunapplication.presentation.ui.theme.PartyRunApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PartyRunApplicationTheme {

                /**
                 * Navigation Architecture Component
                 * 메인 기준 NavHostController 인스턴스 생성
                 */
                val navController = rememberNavController() // 백 스택 관리 및 현재 목적지가 어떤 Composable인지 추적
                SetUpMainGraph(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUpMainGraph(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            SetUpNavGraph(
                startDestination = NavRoutes.Test1.route,
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PartyRunApplicationTheme {
        /*TODO*/
    }
}
