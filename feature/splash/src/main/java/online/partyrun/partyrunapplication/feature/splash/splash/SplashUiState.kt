package online.partyrun.partyrunapplication.feature.splash.splash

import online.partyrun.partyrunapplication.core.model.auth.GoogleUserData

data class SplashUiState(
    val googleUser: GoogleUserData? = null,
    val isAgreementChecked: Boolean = false
)
