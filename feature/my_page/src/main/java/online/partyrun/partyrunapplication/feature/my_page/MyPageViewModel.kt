package online.partyrun.partyrunapplication.feature.my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.auth.GoogleSignOutUseCase
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val googleSignOutUseCase: GoogleSignOutUseCase
): ViewModel() {
    fun signOutFromGoogle() = viewModelScope.launch {
        googleSignOutUseCase()
    }

}
