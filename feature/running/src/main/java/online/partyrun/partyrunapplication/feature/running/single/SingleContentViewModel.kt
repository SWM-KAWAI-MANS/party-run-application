package online.partyrun.partyrunapplication.feature.running.single

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SingleContentViewModel @Inject constructor(

) : ViewModel() {

    companion object {
        const val COUNTDOWN_SECONDS = 5
    }

    private val _singleContentUiState = MutableStateFlow(SingleContentUiState())
    val singleContentUiState: StateFlow<SingleContentUiState> = _singleContentUiState


    suspend fun countDownWhenReady() {
        withContext(Dispatchers.Main) {
            countDown()
            changeScreenToRunning()
        }
    }


    private suspend fun countDown() {
        delay(300) // UI 확인 시간
        for (i in COUNTDOWN_SECONDS downTo 0) {
            delay(1000)
            _singleContentUiState.update { state ->
                state.copy(
                    timeRemaining = i
                )
            }
        }
    }

    private fun changeScreenToRunning() {
        _singleContentUiState.update { state ->
            state.copy(
                screenState = SingleScreenState.Running
            )
        }
    }

}
