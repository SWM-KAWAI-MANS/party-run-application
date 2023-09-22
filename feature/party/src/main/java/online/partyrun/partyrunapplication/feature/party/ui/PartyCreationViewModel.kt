package online.partyrun.partyrunapplication.feature.party.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.domain.party.ConnectPartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.CreatePartyEventSourceListenerUseCase
import online.partyrun.partyrunapplication.core.domain.party.CreatePartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.DisconnectPartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.StartPartyBattleUseCase
import online.partyrun.partyrunapplication.core.model.party.PartyEvent
import online.partyrun.partyrunapplication.feature.party.exception.ManagerProcessException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PartyCreationViewModel @Inject constructor(
    private val createPartyEventSourceListenerUseCase: CreatePartyEventSourceListenerUseCase,
    private val connectPartyEventSourceUseCase: ConnectPartyEventSourceUseCase,
    private val createPartyEventSourceUseCase: CreatePartyEventSourceUseCase,
    private val disconnectPartyEventSourceUseCase: DisconnectPartyEventSourceUseCase,
    private val startPartyBattleUseCase: StartPartyBattleUseCase
) : ViewModel() {

    private val _partyCreationUiState = MutableStateFlow(PartyCreationUiState())
    val partyCreationUiState: StateFlow<PartyCreationUiState> = _partyCreationUiState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private var waitingPartySSEstate =
        CompletableDeferred<Unit>() // SSE 스트림의 상태를 나타내는 CompletableDeferred.

    private val gson = Gson()

    fun quitPartyRoom() {
        disconnectPartyEventSource()
    }

    fun beginManagerProcess(partyCode: String) =
        viewModelScope.launch {
            try {
                connectPartyEventSource(partyCode)
            } catch (e: ManagerProcessException) {
                Timber.tag("PartyViewModel").e(e, "beginManagerProcess 종료")
                clearManagerProcess()
            }
        }

    private suspend fun connectPartyEventSource(partyCode: String) {
        waitingPartySSEstate = CompletableDeferred() //  새로운 Deferred 인스턴스를 사용

        val listener = createPartyEventSourceListenerUseCase(
            onEvent = ::handlePartyEvent,
            onClosed = {
                waitingPartySSEstate.complete(Unit)
            },
            onFailure = {
                try {
                    waitingPartySSEstate.complete(Unit)
                    disconnectPartyEventSource()
                    clearManagerProcess()
                } catch (e: ManagerProcessException) {
                    Timber.e("SSE Waiting 취소")
                }
            }
        )
        connectPartyEventSourceUseCase(
            createPartyEventSourceUseCase(
                listener,
                partyCode
            )
        )
        waitingPartySSEstate.await()
    }

    private fun handlePartyEvent(data: String) {
        val eventData = gson.fromJson(
            data,
            PartyEvent::class.java
        )
        Timber.tag("Event").d("Event Received: $eventData")
        _partyCreationUiState.update { state ->
            state.copy(
                partyEvent = eventData
            )
        }
    }

    fun disconnectPartyEventSource() {
        viewModelScope.launch {
            try {
                disconnectPartyEventSourceUseCase()
                cancelManagerProcess()
            } catch (e: ManagerProcessException) {
                Timber.e("${e.message}")
            }
        }
    }

    private fun clearManagerProcess() {
        waitingPartySSEstate = CompletableDeferred()
        _partyCreationUiState.value = PartyCreationUiState()
    }

    private fun cancelManagerProcess(): Nothing {
        throw ManagerProcessException("failed manager process")
    }

}