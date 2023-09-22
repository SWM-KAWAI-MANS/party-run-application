package online.partyrun.partyrunapplication.feature.party

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
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.party.ConnectPartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.CreatePartyEventSourceListenerUseCase
import online.partyrun.partyrunapplication.core.domain.party.CreatePartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.DisconnectPartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.SendCreatePartyUseCase
import online.partyrun.partyrunapplication.core.domain.party.StartPartyBattleUseCase
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.model.party.PartyEvent
import online.partyrun.partyrunapplication.feature.party.exception.ManagerProcessException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PartyViewModel @Inject constructor(
    private val sendCreatePartyUseCase: SendCreatePartyUseCase,
) : ViewModel() {

    private val _partyUiState = MutableStateFlow(PartyUiState())
    val partyUiState: StateFlow<PartyUiState> = _partyUiState.asStateFlow()

    private val _kmState = MutableStateFlow(KmState.KM_1)
    val kmState: StateFlow<KmState> = _kmState.asStateFlow()

    private val _partyCodeInput = MutableStateFlow("")
    val partyCodeInput = _partyCodeInput.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun setKmState(state: KmState) {
        _kmState.value = state
    }

    fun setPartyCodeInput(number: String) {
        _partyCodeInput.value = number
    }

    fun clearPartyCodeInput() {
        _partyCodeInput.value = ""
    }

    fun createParty(runningDistance: RunningDistance) {
        viewModelScope.launch {
            sendCreatePartyUseCase(runningDistance).collect { result ->
                result.onSuccess { data ->
                    _partyUiState.update { state ->
                        state.copy(
                            partyCode = data.code
                        )
                    }
                }.onFailure { errorMessage, code ->
                    Timber.tag("PartyViewModel").e("$code $errorMessage")
                    _snackbarMessage.value = "파티 생성에 실패하였습니다."
                }
            }
        }
    }

}

