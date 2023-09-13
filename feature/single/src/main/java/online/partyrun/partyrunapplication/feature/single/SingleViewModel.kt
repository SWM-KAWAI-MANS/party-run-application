package online.partyrun.partyrunapplication.feature.single

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SingleViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val INITIAL_DISTANCE = 5000 // 'm' 단위
        const val INITIAL_TIME = 900 // '초' 단위, 900초 = 15분
        const val DISTANCE_INCREMENT = 250
        const val TIME_INCREMENT = 300
        const val SECONDS_IN_HOUR = 3600
        const val SECONDS_IN_MINUTE = 60
    }

    private val _singleUiState = MutableStateFlow<SingleUiState>(SingleUiState.Success)
    val singleUiState: StateFlow<SingleUiState> = _singleUiState

    private val _targetDistance = MutableStateFlow(INITIAL_DISTANCE)
    val targetDistance: StateFlow<Int> = _targetDistance

    private val _targetTime = MutableStateFlow(INITIAL_TIME)
    val targetTime: StateFlow<Int> = _targetTime

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun incrementTargetDistance() {
        _targetDistance.value += DISTANCE_INCREMENT
    }

    fun decrementTargetDistance() {
        if (_targetDistance.value > DISTANCE_INCREMENT) {
            _targetDistance.value -= DISTANCE_INCREMENT
        }
    }

    fun incrementTargetTime() {
        _targetTime.value += TIME_INCREMENT
    }

    fun decrementTargetTime() {
        if (_targetTime.value > TIME_INCREMENT) {
            _targetTime.value -= TIME_INCREMENT
        }
    }

    fun getFormattedTargetTime(): String {
        val hours = _targetTime.value / SECONDS_IN_HOUR
        val remainingTime = _targetTime.value % SECONDS_IN_HOUR
        val minutes = remainingTime / SECONDS_IN_MINUTE
        return String.format("%02d:%02d", hours, minutes)
    }

    fun getFormattedTargetDistance(): String {
        return String.format("%04.2f", (_targetDistance.value.toFloat() / 1000f) * 100f / 100)
    }
}
