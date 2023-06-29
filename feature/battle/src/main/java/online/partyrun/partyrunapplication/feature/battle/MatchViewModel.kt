package online.partyrun.partyrunapplication.feature.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.network.MatchSourceManager
import online.partyrun.partyrunapplication.core.domain.MatchResultEventUseCase
import online.partyrun.partyrunapplication.core.domain.WaitingRunnerEventUseCase
import online.partyrun.partyrunapplication.core.model.match.MatchResultEventResponse
import online.partyrun.partyrunapplication.core.model.match.WaitingRunnerEventResponse
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val waitingRunnerEventUseCase: WaitingRunnerEventUseCase,
    private val matchResultEventUseCase: MatchResultEventUseCase,
    private val MatchSourceManager: MatchSourceManager
): ViewModel() {
    private val _waitingRunnerEventState = MutableStateFlow(WaitingRunnerState())
    val waitingRunnerEventState: StateFlow<WaitingRunnerState> = _waitingRunnerEventState.asStateFlow()

    private val _matchResultEventState = MutableStateFlow(MatchResultState())
    val matchResultEventState: StateFlow<MatchResultState> = _matchResultEventState.asStateFlow()

    private val gson = Gson()
    private fun handleMatchEvent(data: String) {
        val eventData = gson.fromJson(data, WaitingRunnerEventResponse::class.java)
        Timber.tag("Event").d("Event Received: isSuccess -: ${eventData.isSuccess}")
        Timber.tag("Event").d("Event Received: message -: ${eventData.message}")
        _waitingRunnerEventState.update {
            it.copy(
                isSuccess = eventData.isSuccess,
                message = eventData.message
            )
        }
    }

    private fun handleMatchResultEvent(data: String) {
        val eventData = gson.fromJson(data, MatchResultEventResponse::class.java)
        Timber.tag("Event").d("Event Received: status -: ${eventData.status}")
        Timber.tag("Event").d("Event Received: location -: ${eventData.location}")
        _matchResultEventState.update {
            it.copy(
                status = eventData.status,
                location = eventData.location
            )
        }
    }
    fun connectMatchEventSource() {
        viewModelScope.launch {
            val listener = MatchSourceManager.getMatchEventSourceListener(
                onEvent = ::handleMatchEvent
            )
            MatchSourceManager.connectMatchEventSource(waitingRunnerEventUseCase(listener))
        }
    }

    fun connectMatchResultEventSource() {
        viewModelScope.launch {
            val listener = MatchSourceManager.getMatchEventSourceListener(
                onEvent = ::handleMatchResultEvent
            )
            MatchSourceManager.connectMatchResultEventSource(matchResultEventUseCase(listener))
        }
    }

    fun closeMatchEventSource() {
        viewModelScope.launch {
            MatchSourceManager.closeMatchEventSource()
        }
    }

    fun closeMatchResultEventSource() {
        viewModelScope.launch {
            MatchSourceManager.closeMatchResultEventSource()
        }
    }

}
