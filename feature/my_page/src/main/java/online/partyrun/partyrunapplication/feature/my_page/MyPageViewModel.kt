package online.partyrun.partyrunapplication.feature.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.agreement.SaveAgreementStateUseCase
import online.partyrun.partyrunapplication.core.domain.auth.GoogleSignOutUseCase
import online.partyrun.partyrunapplication.core.domain.my_page.GetMyPageDataUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val googleSignOutUseCase: GoogleSignOutUseCase,
    private val saveAgreementUseCase: SaveAgreementStateUseCase,
    private val getMyPageDataUseCase: GetMyPageDataUseCase
): ViewModel() {

    private val _myPageUiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val myPageUiState = _myPageUiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val user = getMyPageDataUseCase()
                _myPageUiState.value = MyPageUiState.Success(user = user)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun signOutFromGoogle() = viewModelScope.launch {
        googleSignOutUseCase()
    }

    fun saveAgreementState(isChecked: Boolean) = viewModelScope.launch {
        saveAgreementUseCase(isChecked)
    }


}
