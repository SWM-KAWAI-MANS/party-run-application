package online.partyrun.partyrunapplication.feature.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.core.designsystem.component.CenterCircularProgressIndicator

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    setIntentMainActivity: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        delay(1500L)
        setIntentMainActivity()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        CenterCircularProgressIndicator()
    }
}