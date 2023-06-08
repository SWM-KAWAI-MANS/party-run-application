package online.partyrun.partyrunapplication.presentation.auth.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.partyrun.partyrunapplication.presentation.components.CenterCircularProgressIndicator

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        CenterCircularProgressIndicator()
    }
}