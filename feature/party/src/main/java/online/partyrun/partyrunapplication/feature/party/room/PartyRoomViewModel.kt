package online.partyrun.partyrunapplication.feature.party.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.result.onEmpty
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.match.GetRunnersInfoUseCase
import online.partyrun.partyrunapplication.core.domain.match.SaveRunnersInfoUseCase
import online.partyrun.partyrunapplication.core.domain.party.ConnectPartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.CreatePartyEventSourceListenerUseCase
import online.partyrun.partyrunapplication.core.domain.party.CreatePartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.DisconnectPartyEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.party.QuitPartyUseCase
import online.partyrun.partyrunapplication.core.domain.party.StartPartyBattleUseCase
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.model.party.PartyEvent
import online.partyrun.partyrunapplication.core.model.party.PartyEventStatus
import online.partyrun.partyrunapplication.feature.party.exception.ManagerProcessException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PartyRoomViewModel @Inject constructor(
    private val createPartyEventSourceListenerUseCase: CreatePartyEventSourceListenerUseCase,
    private val connectPartyEventSourceUseCase: ConnectPartyEventSourceUseCase,
    private val createPartyEventSourceUseCase: CreatePartyEventSourceUseCase,
    private val disconnectPartyEventSourceUseCase: DisconnectPartyEventSourceUseCase,
    private val getRunnersInfoUseCase: GetRunnersInfoUseCase,
    private val saveRunnersInfoUseCase: SaveRunnersInfoUseCase,
    private val startPartyBattleUseCase: StartPartyBattleUseCase,
    private val quitPartyUseCase: QuitPartyUseCase
) : ViewModel() {

    private val _partyRoomUiState = MutableStateFlow<PartyRoomUiState>(PartyRoomUiState.Loading)
    val partyRoomUiState: StateFlow<PartyRoomUiState> = _partyRoomUiState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private var waitingPartySSEstate =
        CompletableDeferred<Unit>() // SSE 스트림의 상태를 나타내는 CompletableDeferred.

    private val gson = Gson()
    var runnerInfoData = RunnerInfoData(emptyList())
    var updatedPartyRoomState = PartyRoomState()

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun beginPartyProcess(partyCode: String) =
        viewModelScope.launch {
            try {
                connectPartyEventSource(partyCode)
            } catch (e: ManagerProcessException) {
                Timber.tag("PartyViewModel").e(e, "beginPartyProcess 종료")
                disconnectPartyEventSource()
                clearPartyProcess()
            }
        }

    private suspend fun connectPartyEventSource(partyCode: String) {
        waitingPartySSEstate = CompletableDeferred() //  새로운 Deferred 인스턴스를 사용

        val listener = createPartyEventSourceListenerUseCase(
            onEvent = ::handlePartyEvent,
            onClosed = {
                Timber.tag("PartyRoomViewModel").e("정상적인 sse closed")
                waitingPartySSEstate.complete(Unit)
            },
            onFailure = {
                // disconnect가 이루어지므로 clear만 수행
                waitingPartySSEstate.complete(Unit)
                failedProcess()
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
        val eventData = gson.fromJson(data, PartyEvent::class.java)
        Timber.tag("Event").d("Event Received: $eventData")
        val allRunnerIds = RunnerIds(eventData.participantIds)

        when (eventData.status) {
            PartyEventStatus.WAITING -> { // 파티룸에서의 처리
                waitingEventProcess(allRunnerIds, eventData)
            }

            PartyEventStatus.COMPLETED -> { // 파티 시작시 처리
                completedEventProcess(eventData)
            }

            PartyEventStatus.CANCELLED -> { // 파티 해체시 처리
                canceledEventProcess(eventData)
            }
        }
    }

    private fun completedEventProcess(eventData: PartyEvent) {
        viewModelScope.launch {
            saveRunnersInfoUseCase(runnerInfoData)
        }
        updatedPartyRoomState = updatePartyRoomState(runnerInfoData, eventData)
        _partyRoomUiState.value =
            _partyRoomUiState.value.updateState(updatedPartyRoomState)
    }

    private fun canceledEventProcess(eventData: PartyEvent) {
        updatedPartyRoomState = updatePartyRoomState(runnerInfoData, eventData)
        _partyRoomUiState.value =
            _partyRoomUiState.value.updateState(updatedPartyRoomState)
        _snackbarMessage.value = "파티 세션이 종료됐어요."
    }

    private fun waitingEventProcess(
        allRunnerIds: RunnerIds,
        eventData: PartyEvent
    ) {
        viewModelScope.launch {
            getRunnersInfoUseCase(allRunnerIds).collect { result ->
                result.onSuccess { runnerInfoList ->
                    runnerInfoData = runnerInfoList
                    updatedPartyRoomState = updatePartyRoomState(runnerInfoData, eventData)

                    // 현재 상태가 Loading인 경우에만 Success로 상태 변경
                    if (_partyRoomUiState.value is PartyRoomUiState.Loading) {
                        _partyRoomUiState.value =
                            PartyRoomUiState.Success(partyRoomState = updatedPartyRoomState)
                    } else {
                        _partyRoomUiState.value =
                            _partyRoomUiState.value.updateState(updatedPartyRoomState)
                    }
                }
            }
        }
    }

    private fun updatePartyRoomState(
        runnerInfoList: RunnerInfoData,
        eventData: PartyEvent
    ): PartyRoomState {
        // 여기서 runnerInfoList는 RunnerInfo의 List라고 가정합니다.
        val managerInfo = runnerInfoList.runners.find { it.id == eventData.managerId }
        val participantsInfo = runnerInfoList.runners.filter { it.id in eventData.participantIds }

        return PartyRoomState(
            entryCode = eventData.entryCode,
            distance = eventData.distance,
            status = eventData.status,
            battleId = eventData.battleId,
            manager = managerInfo,
            participants = participantsInfo
        )
    }

    fun startPartyBattle(partyCode: String) {
        if (!isEligibleToStartBattle()) {
            _snackbarMessage.value = "파티 인원이 2명 이상 필요해요."
            return
        }

        viewModelScope.launch {
            startPartyBattleUseCase(partyCode).collect { result ->
                result.onEmpty {
                    Timber.tag("파티 배틀").d("startPartyBattle 성공")
                }.onFailure { errorMessage, code ->
                    Timber.e("$code $errorMessage")
                    disconnectPartyEventSourceUseCase()
                }
            }
        }
    }

    private fun isEligibleToStartBattle(): Boolean {
        val currentState = _partyRoomUiState.value
        if (currentState is PartyRoomUiState.Success && currentState.partyRoomState.participants.size < 2) {
            return false
        }
        return true
    }

    fun quitPartyRoom(partyCode: String) {
        viewModelScope.launch {
            quitPartyUseCase(partyCode).collect { result ->
                result.onEmpty {
                    Timber.tag("파티 배틀").d("나가기 성공")
                    quitProcess()
                }.onFailure { errorMessage, code ->
                    Timber.e("$code $errorMessage")
                    _snackbarMessage.value = "파티 나가기 실패 \n잠시 후 다시 시도 해주세요."
                }
            }
        }
    }

    private fun quitProcess() {
        val currentState = _partyRoomUiState.value
        if (currentState is PartyRoomUiState.Success) {
            val updatedState = currentState.partyRoomState.copy(status = PartyEventStatus.CANCELLED)
            _partyRoomUiState.value = currentState.copy(partyRoomState = updatedState)
        }
    }

    private fun disconnectPartyEventSource() {
        disconnectPartyEventSourceUseCase()
    }

    private fun clearPartyProcess() {
        waitingPartySSEstate = CompletableDeferred()
        _partyRoomUiState.value = PartyRoomUiState.Loading
    }

    private fun failedProcess() {
        _snackbarMessage.value = "파티 세션이 종료됐어요."
        _partyRoomUiState.value = PartyRoomUiState.LoadFailed
    }

    fun preparingMenuMessage() {
        _snackbarMessage.value = "메뉴 기능 준비 중이에요."
    }

    fun copyToClipboardMessage() {
        _snackbarMessage.value = "번호가 복사되었어요.\n친구에게 붙여넣기 해주세요."
    }

    override fun onCleared() {
        super.onCleared()
        clearPartyProcess()
    }

}
