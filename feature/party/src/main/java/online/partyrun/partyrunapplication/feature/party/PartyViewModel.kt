package online.partyrun.partyrunapplication.feature.party

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PartyViewModel @Inject constructor(

) : ViewModel() {
    private val _kmState = MutableStateFlow(KmState.KM_1)

    private val _partyRoomNumber = MutableStateFlow("")
    val partyRoomNumber = _partyRoomNumber.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun setKmState(state: KmState) {
        _kmState.value = state
    }

    fun setPartyRoomNumber(number: String) {
        _partyRoomNumber.value = number
    }

    fun clearPartyRoomNumber() {
        _partyRoomNumber.value = ""
    }

}
