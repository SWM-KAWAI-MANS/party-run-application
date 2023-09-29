package online.partyrun.partyrunapplication.feature.single

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import online.partyrun.partyrunapplication.core.model.single.SingleTargetDetails
import online.partyrun.partyrunapplication.core.model.single.decrementDistance
import online.partyrun.partyrunapplication.core.model.single.decrementTime
import online.partyrun.partyrunapplication.core.model.single.getFormattedDistance
import online.partyrun.partyrunapplication.core.model.single.getFormattedTime
import online.partyrun.partyrunapplication.core.model.single.incrementDistance
import online.partyrun.partyrunapplication.core.model.single.incrementTime
import javax.inject.Inject

@HiltViewModel
class SingleViewModel @Inject constructor(

) : ViewModel() {

    private val _singleUiState = MutableStateFlow<SingleUiState>(SingleUiState.Success)
    val singleUiState: StateFlow<SingleUiState> = _singleUiState

    private val _singleTargetDetails = MutableStateFlow(SingleTargetDetails())
    val singleTargetDetails: StateFlow<SingleTargetDetails> = _singleTargetDetails

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun incrementTargetDistance() {
        _singleTargetDetails.value = _singleTargetDetails.value.incrementDistance()
    }

    fun decrementTargetDistance() {
        _singleTargetDetails.value = _singleTargetDetails.value.decrementDistance()
    }

    fun incrementTargetTime() {
        _singleTargetDetails.value = _singleTargetDetails.value.incrementTime()
    }

    fun decrementTargetTime() {
        _singleTargetDetails.value = _singleTargetDetails.value.decrementTime()
    }

    fun getFormattedTargetTime(): String {
        return _singleTargetDetails.value.getFormattedTime()
    }

    fun getFormattedTargetDistance(): String {
        return _singleTargetDetails.value.getFormattedDistance()
    }

}
