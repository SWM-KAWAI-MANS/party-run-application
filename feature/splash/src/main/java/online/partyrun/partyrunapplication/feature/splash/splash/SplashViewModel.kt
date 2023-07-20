package online.partyrun.partyrunapplication.feature.splash.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.GetAgreementStateUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAgreementStateUseCase: GetAgreementStateUseCase
): ViewModel() {

    private val _isAgreement = MutableStateFlow(false)
    val isAgreement: StateFlow<Boolean> = _isAgreement.asStateFlow()

    init {
        getAgreementState()
    }

    private fun getAgreementState() = viewModelScope.launch {
        getAgreementStateUseCase().collect { checked ->
            _isAgreement.value = checked
        }
    }
}
