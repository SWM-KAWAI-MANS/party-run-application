package online.partyrun.partyrunapplication.feature.single

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SingleViewModel @Inject constructor(

) : ViewModel() {
    private val _singleUiState = MutableStateFlow<SingleUiState>(SingleUiState.Success)
    val singleUiState: StateFlow<SingleUiState> = _singleUiState

    private val _targetDistance = MutableStateFlow(5000)
    val targetDistance: StateFlow<Int> = _targetDistance

    private val _targetTime = MutableStateFlow(15)
    val targetTime: StateFlow<Int> = _targetTime

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun incrementTargetDistance() {
        _targetDistance.value += 250
    }

    fun decrementTargetDistance() {
        if (_targetDistance.value >= 250) {
            _targetDistance.value -= 250
        }
    }

    fun incrementTargetTime() {
        _targetTime.value += 5
    }

    fun decrementTargetTime() {
        if (_targetTime.value > 5) {
            _targetTime.value -= 5
        }
    }

    fun getFormattedTargetTime(): String {
        val hours = _targetTime.value / 60
        val minutes = _targetTime.value % 60
        return String.format("%02d:%02d", hours, minutes)
    }

    fun getFormattedTargetDistance(): String {
        return String.format("%04.2f", (_targetDistance.value.toFloat() / 1000f) * 100f / 100)
    }
}
