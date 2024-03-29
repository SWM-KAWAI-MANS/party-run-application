package online.partyrun.partyrunapplication.feature.splash.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import online.partyrun.partyrunapplication.core.designsystem.component.PartyRunGradientBackground
import online.partyrun.partyrunapplication.core.designsystem.theme.LocalGradientColors
import online.partyrun.partyrunapplication.feature.splash.R

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = hiltViewModel(),
    setIntentMainActivity: () -> Unit,
    navigateToAgreement: () -> Unit
) {
    val splashUiState by splashViewModel.splashUiState.collectAsStateWithLifecycle()

    LaunchedEffect(splashUiState) {
        showSplashForDuration(1500L)

        if (isUserLoggedInAndAgreed(splashUiState)) { // Google 로그인이 되어있는 상태라면
            setIntentMainActivity()
        } else {
            navigateToAgreement()
        }
    }

    PartyRunGradientBackground(
        gradientColors = LocalGradientColors.current
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo_content_desc)
            )
        }
    }
}

private fun isUserLoggedInAndAgreed(state: SplashUiState): Boolean {
    return state.googleUser != null && state.isAgreementChecked
}

private suspend fun showSplashForDuration(duration: Long) {
    delay(duration)
}
