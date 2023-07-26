package online.partyrun.partyrunapplication.feature.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.common.network.MatchSourceManager
import online.partyrun.partyrunapplication.core.domain.GetMatchResultEventUseCase
import online.partyrun.partyrunapplication.core.domain.SendWaitingMatchUseCase
import online.partyrun.partyrunapplication.core.domain.GetWaitingEventUseCase
import online.partyrun.partyrunapplication.core.domain.SendAcceptMatchUseCase
import online.partyrun.partyrunapplication.core.domain.SendDeclineMatchUseCase
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.MatchResultEventResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import online.partyrun.partyrunapplication.core.model.match.WaitingEventResult
import online.partyrun.partyrunapplication.core.model.match.WaitingMatchStatus
import online.partyrun.partyrunapplication.feature.match.exception.MatchingProcessException
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val getWaitingEventUseCase: GetWaitingEventUseCase,
    private val getMatchResultEventUseCase: GetMatchResultEventUseCase,
    private val sendWaitingMatchUseCase: SendWaitingMatchUseCase,
    private val sendAcceptMatchUseCase: SendAcceptMatchUseCase,
    private val sendDeclineMatchUseCase: SendDeclineMatchUseCase,
    private val matchSourceManager: MatchSourceManager
) : ViewModel() {
    private val _matchUiState = MutableStateFlow(MatchUiState())
    val matchUiState: StateFlow<MatchUiState> = _matchUiState.asStateFlow()

    private var matchEventSSEstate = CompletableDeferred<Unit>() // SSE 스트림의 상태를 나타내는 CompletableDeferred.
    private var matchResultEventSSEstate = CompletableDeferred<Unit>() // SSE 스트림의 상태를 나타내는 CompletableDeferred.
    private val gson = Gson()
    private var userDecision = MutableStateFlow<Boolean?>(null)

    fun onUserDecision(isAccepted: Boolean) {
        userDecision.value = isAccepted
    }

    fun openMatchDialog() {
        _matchUiState.update { state ->
            state.copy(isOpen = true)
        }
    }

    fun closeMatchDialog() {
        matchEventSSEstate = CompletableDeferred()
        matchResultEventSSEstate = CompletableDeferred()
        userDecision.value = null
        _matchUiState.value = MatchUiState()
    }

    /**
     * Battle 과정에 대한 전체 프로세스 수행
     * 1. 배틀 매칭 큐에 사용자 등록
     * 2. 해당 큐에 목표한 사용자 수만큼 등록 완료 시
     * 3. 수락/거절 여부 파악
     * 4. 최종 매칭 완료 프로세스 진행
     */
    fun beginBattleMatchingProcess(userSelectedMatchDistance: UserSelectedMatchDistance) = viewModelScope.launch {
        try {
            registerMatch(userSelectedMatchDistance)
            connectMatchEventSource()
            handleUserMatchDecision()
            connectMatchResultEventSource()
            verifyMatchSuccess()
        } catch(e: MatchingProcessException) {
            /* 예외 발생 시 배틀 매칭 과정을 종료하고 상태 초기화 과정 수행 */
            Timber.tag("MatchViewModel").e(e, "매칭 거절")
            closeMatchDialog()
        }
    }

    private fun verifyMatchSuccess() {
        val matchStatus = matchUiState.value.matchResultEventState.status
        if (matchStatus == MatchResultStatus.SUCCESS) {
            _matchUiState.update { state ->
                state.copy(
                    isAllRunnersAccepted = true
                )
            }
        } else {
            cancelMatchingProcess()
        }
    }

    private suspend fun handleUserMatchDecision() {
        val status = matchUiState.value.waitingEventState.status
        if (status != WaitingMatchStatus.MATCHED) {
            cancelMatchingProcess()
        }
        val decision = waitForUserDecision() // (수락 = true, 거절 = false)
        decision.let {
            if (it) {
                acceptMatch(MatchDecisionRequest(isJoin = true))
            } else {
                declineMatch(MatchDecisionRequest(isJoin = false))
            }
        }
    }

    private fun cancelMatchingProcess(): Nothing {
        throw MatchingProcessException("Closing match dialog and End of the matching coroutine process")
    }

    /* REST */
    private suspend fun registerMatch(userSelectedMatchDistance: UserSelectedMatchDistance) =
        sendWaitingMatchUseCase(userSelectedMatchDistance).collect {
            when(it) {
                is ApiResponse.Success -> {
                    _matchUiState.update { state ->
                        state.copy(
                            WaitingRestState = WaitingRestState(
                                message = it.data.message
                            ),
                        )
                    }
                }
                is ApiResponse.Failure -> {
                    Timber.tag("MatchViewModel").e("${it.code} ${it.errorMessage}")
                    throw MatchingProcessException("Failure from API: ${it.errorMessage}")
                }
                ApiResponse.Loading ->  {
                    Timber.tag("MatchViewModel").d("Loading")
                }
            }
        }
    private suspend fun acceptMatch(matchDecisionRequest: MatchDecisionRequest) =
        sendAcceptMatchUseCase(matchDecisionRequest).collect() {
            when(it) {
                is ApiResponse.Success -> {
                    _matchUiState.update { state ->
                        state.copy(
                            matchProgress = MatchProgress.RESULT,
                            matchResultRestState = MatchResultRestState(
                                message = it.data.message
                            )
                        )
                    }
                }
                is ApiResponse.Failure -> {
                    Timber.tag("MatchViewModel").e("${it.code} ${it.errorMessage}")
                    throw RuntimeException("Failure from API: ${it.errorMessage}")
                }
                ApiResponse.Loading ->  {
                    Timber.tag("MatchViewModel").d("Loading")
                }
            }
        }

    private suspend fun declineMatch(matchDecisionRequest: MatchDecisionRequest) =
        sendDeclineMatchUseCase(matchDecisionRequest).collect() {
            when(it) {
                is ApiResponse.Success -> {
                    _matchUiState.update { state ->
                        state.copy(
                            matchProgress = MatchProgress.CANCEL,
                            matchResultRestState = MatchResultRestState(
                                message = it.data.message
                            )
                        )
                    }
                }
                is ApiResponse.Failure -> {
                    Timber.tag("MatchViewModel").e("${it.code} ${it.errorMessage}")
                    throw RuntimeException("Failure from API: ${it.errorMessage}")
                }
                ApiResponse.Loading ->  {
                    Timber.tag("MatchViewModel").d("Loading")
                }
            }
        }

    private suspend fun waitForUserDecision(): Boolean {
        withTimeoutOrNull(TimeUnit.SECONDS.toMillis(10)) {
            while (userDecision.value == null) {
                delay(100) // delay a little bit
            }
        }
        return userDecision.value ?: false // 10초 동안 무응답이면 거절이라 판단
    }

    /* SSE */
    private fun handleMatchEvent(data: String) {
        val eventData = gson.fromJson(data, WaitingEventResult::class.java)
        Timber.tag("Event").d("Event Received: status -: ${eventData.status}")
        Timber.tag("Event").d("Event Received: message -: ${eventData.message}")
        _matchUiState.update {
            it.copy(
                matchProgress = if (eventData.status == WaitingMatchStatus.MATCHED) MatchProgress.DECISION else it.matchProgress,
                waitingEventState = WaitingEventState(
                    status = eventData.status,
                    message = eventData.message
                )
            )
        }
    }

    private fun handleMatchResultEvent(data: String) {
        val eventData = gson.fromJson(data, MatchResultEventResult::class.java)
        Timber.tag("Event").d("Event Received: status -: ${eventData.status}")
        Timber.tag("Event").d("Event Received: members -: ${eventData.members}")
        val status = MatchResultStatus.fromString(eventData.status)
        status?.let { resultStatus ->
            _matchUiState.update {
                it.copy(
                    matchResultEventState = MatchResultEventState(
                        status = resultStatus,
                        members = eventData.members
                    )
                )
            }
        }
    }

    /**
     * connectMatch____EventSource() =
     * SSE 스트림에 연결하고, 스트림에서 매치 결과 이벤트를 받아 처리하며, 스트림이 종료될 때까지 대기하는 역할 수행
     * onEvent 콜백은 매치 결과 이벤트가 발생할 때 호출
     * onClosed 콜백은 SSE 스트림이 종료되었을 때 호출되며, 이 때 sseState의 상태를 완료(complete)로 변경
     * sseState.await(): 현재 스트림의 상태(sseState)가 완료 상태가 될 때까지 대기
     */
    private suspend fun connectMatchEventSource() {
        val listener = matchSourceManager.createMatchEventSourceListener(
            onEvent = ::handleMatchEvent,
            onClosed = {
                matchEventSSEstate.complete(Unit)
            }
        )
        matchSourceManager.connectMatchEventSource(getWaitingEventUseCase(listener))
        matchEventSSEstate.await()
    }

    private suspend fun connectMatchResultEventSource() {
        val listener = matchSourceManager.createMatchEventSourceListener(
            onEvent = ::handleMatchResultEvent,
            onClosed = {
                matchResultEventSSEstate.complete(Unit)
            }
        )
        matchSourceManager.connectMatchResultEventSource(getMatchResultEventUseCase(listener))
        matchResultEventSSEstate.await()
    }

    fun disconnectMatchEventSource() {
        viewModelScope.launch {
            try {
                matchSourceManager.disconnectMatchEventSource()
                cancelMatchingProcess()
            } catch (e: MatchingProcessException) {
                Timber.e("${e.message}")
            }
        }
    }

    fun disconnectMatchResultEventSource() {
        viewModelScope.launch {
            try {
                matchSourceManager.disconnectMatchResultEventSource()
                cancelMatchingProcess()
            } catch (e: MatchingProcessException) {
                Timber.e("${e.message}")
            }
        }
    }
}
