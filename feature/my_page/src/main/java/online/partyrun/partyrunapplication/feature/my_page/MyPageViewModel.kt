package online.partyrun.partyrunapplication.feature.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.agreement.SaveAgreementStateUseCase
import online.partyrun.partyrunapplication.core.domain.auth.GoogleSignOutUseCase
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val googleSignOutUseCase: GoogleSignOutUseCase,
    private val saveAgreementUseCase: SaveAgreementStateUseCase
): ViewModel() {
    fun signOutFromGoogle() = viewModelScope.launch {
        googleSignOutUseCase()
    }

    fun saveAgreementState(isChecked: Boolean) = viewModelScope.launch {
        saveAgreementUseCase(isChecked)
    }


}
