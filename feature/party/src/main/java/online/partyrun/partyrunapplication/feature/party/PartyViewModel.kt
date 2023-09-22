package online.partyrun.partyrunapplication.feature.party

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import javax.inject.Inject

@HiltViewModel
class PartyViewModel @Inject constructor(

) : ViewModel() {
    private val _kmState = MutableStateFlow(KmState.KM_1)

    private val _partyCode = MutableStateFlow("")
    val partyCode = _partyCode.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun setKmState(state: KmState) {
        _kmState.value = state
    }

    fun setPartyCodeInput(number: String) {
        _partyCode.value = number
    }

    fun clearPartyCodeInput() {
        _partyCode.value = ""
    }
}
