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
import online.partyrun.partyrunapplication.core.domain.match.CancelMatchWaitingUseCase
import online.partyrun.partyrunapplication.core.domain.match.ConnectMatchResultEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.match.ConnectWaitingEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.match.CreateMatchEventSourceListenerUseCase
import online.partyrun.partyrunapplication.core.domain.match.CreateMatchResultEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.match.SendRegisterMatchUseCase
import online.partyrun.partyrunapplication.core.domain.match.CreateWaitingEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.match.DisconnectMatchResultEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.match.DisconnectWaitingEventSourceUseCase
import online.partyrun.partyrunapplication.core.domain.match.GetRunnerIdsUseCase
import online.partyrun.partyrunapplication.core.domain.match.GetRunnersInfoUseCase
import online.partyrun.partyrunapplication.core.domain.match.SaveRunnersInfoUseCase
import online.partyrun.partyrunapplication.core.domain.match.SendAcceptMatchUseCase
import online.partyrun.partyrunapplication.core.domain.match.SendDeclineMatchUseCase
import online.partyrun.partyrunapplication.core.model.match.MatchDecision
import online.partyrun.partyrunapplication.core.model.match.MatchResultEvent
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.model.match.WaitingEvent
import online.partyrun.partyrunapplication.core.model.match.WaitingStatus
import online.partyrun.partyrunapplication.feature.match.exception.MatchingProcessException
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val sendRegisterMatchUseCase: SendRegisterMatchUseCase,
    private val sendAcceptMatchUseCase: SendAcceptMatchUseCase,
    private val sendDeclineMatchUseCase: SendDeclineMatchUseCase,
    private val createMatchEventSourceListenerUseCase: CreateMatchEventSourceListenerUseCase,
    private val createWaitingEventSourceUseCase: CreateWaitingEventSourceUseCase,
    private val createMatchResultEventSourceUseCase: CreateMatchResultEventSourceUseCase,
    private val connectWaitingEventSourceUseCase: ConnectWaitingEventSourceUseCase,
    private val connectMatchResultEventSourceUseCase: ConnectMatchResultEventSourceUseCase,
    private val disconnectWaitingEventSourceUseCase: DisconnectWaitingEventSourceUseCase,
    private val disconnectMatchResultEventSourceUseCase: DisconnectMatchResultEventSourceUseCase,
    private val getRunnerIdsUseCase: GetRunnerIdsUseCase,
    private val getRunnersInfoUseCase: GetRunnersInfoUseCase,
    private val saveRunnersInfoUseCase: SaveRunnersInfoUseCase,
    private val cancelMatchWaitingUseCase: CancelMatchWaitingUseCase
) : ViewModel() {

    private val _matchUiState = MutableStateFlow(MatchUiState())
    val matchUiState: StateFlow<MatchUiState> = _matchUiState.asStateFlow()

    private var waitingEventSSEstate =
        CompletableDeferred<Unit>() // SSE 스트림의 상태를 나타내는 CompletableDeferred.
    private var matchResultEventSSEstate =
        CompletableDeferred<Unit>() // SSE 스트림의 상태를 나타내는 CompletableDeferred.
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
        waitingEventSSEstate = CompletableDeferred()
        matchResultEventSSEstate = CompletableDeferred()
        userDecision.value = null
        _matchUiState.value = MatchUiState()
    }

    /**
     * Battle 매칭 과정에 대한 전체 프로세스 수행
     * 1. 배틀 매칭 큐에 사용자 등록
     * 2. 해당 큐에 목표한 사용자 수만큼 등록 완료 시
     * 3. 수락/거절 여부 파악
     * 4. 러너 데이터 가져오기
     * 5. 매칭에 참여한 러너들의 Name, Profile URL 획득 및 저장
     * 6. 다른 러너들의 매칭여부 판단
     * 7. 최종 매칭 완료 프로세스 진행
     */
    fun beginBattleMatchingProcess(runningDistance: RunningDistance) =
        viewModelScope.launch {
            try {
                registerMatch(runningDistance)
                connectWaitingEventSource()
                handleUserMatchDecision()
                getRunnerIds()
                saveRunnersInfo(_matchUiState.value.runnerIds)
                connectMatchResultEventSource()
                verifyMatchSuccess()
            } catch (e: MatchingProcessException) {
                /* 예외 발생 시 배틀 매칭 과정을 종료하고 상태 초기화 과정 수행 */
                Timber.tag("MatchViewModel").e(e, "매칭 거절")
                closeMatchDialog()
            }
        }

    private suspend fun getRunnerIds() =
        getRunnerIdsUseCase().collect {
            when (it) {
                is ApiResponse.Success -> {
                    _matchUiState.update {state ->
                        state.copy(
                            runnerIds = it.data
                        )
                    }
                }

                is ApiResponse.Failure -> {
                    Timber.tag("MainViewModel getRunnerIds").e("${it.code} ${it.errorMessage}")
                    cancelMatchingProcess()
                }

                ApiResponse.Loading -> {
                    Timber.d("$it")
                }
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
        if (status != WaitingStatus.MATCHED) {
            cancelMatchingProcess()
        }
        val decision = waitForUserDecision() // (수락 = true, 거절 = false)
        decision.let {
            if (it) {
                acceptMatch(MatchDecision(isJoin = true))
            } else {
                declineMatch(MatchDecision(isJoin = false))
            }
        }
    }

    private fun cancelMatchingProcess(): Nothing {
        throw MatchingProcessException("Closing match dialog and End of the matching coroutine process")
    }

    /* REST */
    private suspend fun registerMatch(runningDistance: RunningDistance) =
        sendRegisterMatchUseCase(runningDistance).collect {
            when (it) {
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
                    cancelMatchingProcess()
                }

                ApiResponse.Loading -> {
                    Timber.tag("MatchViewModel").d("Loading")
                }
            }
        }

    private suspend fun acceptMatch(matchDecision: MatchDecision) =
        sendAcceptMatchUseCase(matchDecision).collect() {
            when (it) {
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
                    cancelMatchingProcess()
                }

                ApiResponse.Loading -> {
                    Timber.tag("MatchViewModel").d("Loading")
                }
            }
        }

    private suspend fun declineMatch(matchDecision: MatchDecision) =
        sendDeclineMatchUseCase(matchDecision).collect() {
            when (it) {
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
                    cancelMatchingProcess()
                }

                ApiResponse.Loading -> {
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
    private fun handleWaitingEvent(data: String) {
        val eventData = gson.fromJson(
            data,
            WaitingEvent::class.java
        )
        Timber.tag("Event").d("Event Received: status -: ${eventData.status}")
        _matchUiState.update {
            it.copy(
                matchProgress = if (eventData.status == WaitingStatus.MATCHED) MatchProgress.DECISION else it.matchProgress,
                waitingEventState = WaitingEventState(
                    status = eventData.status
                )
            )
        }
    }

    private fun handleMatchResultEvent(data: String) {
        val eventData = gson.fromJson(
            data,
            MatchResultEvent::class.java
        )
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
     * connect_________EventSource() =
     * SSE 스트림에 연결하고, 스트림에서 매치 결과 이벤트를 받아 처리하며, 스트림이 종료될 때까지 대기하는 역할 수행
     * onEvent 콜백은 매치 결과 이벤트가 발생할 때 호출
     * onClosed 콜백은 SSE 스트림이 종료되었을 때 호출되며, 이 때 sseState의 상태를 완료(complete)로 변경
     * sseState.await(): 현재 스트림의 상태(sseState)가 완료 상태가 될 때까지 대기
     */
    private suspend fun connectWaitingEventSource() {
        val listener = createMatchEventSourceListenerUseCase(
            onEvent = ::handleWaitingEvent,
            onClosed = {
                waitingEventSSEstate.complete(Unit)
            },
            onFailure = {
                waitingEventSSEstate.complete(Unit)
            }
        )
        connectWaitingEventSourceUseCase(createWaitingEventSourceUseCase(listener))
        waitingEventSSEstate.await()
    }

    private suspend fun connectMatchResultEventSource() {
        val listener = createMatchEventSourceListenerUseCase(
            onEvent = ::handleMatchResultEvent,
            onClosed = {
                matchResultEventSSEstate.complete(Unit)
            },
            onFailure = {
                matchResultEventSSEstate.complete(Unit)
            }
        )
        connectMatchResultEventSourceUseCase(createMatchResultEventSourceUseCase(listener))
        matchResultEventSSEstate.await()
    }

    fun disconnectMatchEventSource() {
        viewModelScope.launch {
            try {
                disconnectWaitingEventSourceUseCase()
                cancelMatchingProcess()
            } catch (e: MatchingProcessException) {
                Timber.e("${e.message}")
            }
        }
    }

    fun disconnectMatchResultEventSource() {
        viewModelScope.launch {
            try {
                disconnectMatchResultEventSourceUseCase()
                cancelMatchingProcess()
            } catch (e: MatchingProcessException) {
                Timber.e("${e.message}")
            }
        }
    }

    private suspend fun saveRunnersInfo(runnerIds: RunnerIds) =
        getRunnersInfoUseCase(runnerIds).collect {
            when (it) {
                is ApiResponse.Success -> {
                    saveRunnersInfoUseCase(it.data)
                    _matchUiState.update {state ->
                        state.copy(
                            runnerInfoData = it.data
                        )
                    }
                }

                is ApiResponse.Failure -> {
                    Timber.tag("MatchViewModel").e("${it.code} ${it.errorMessage}")
                    cancelMatchingProcess()
                }

                ApiResponse.Loading -> {
                    Timber.d("$it")
                }
            }
        }

    fun cancelMatchWaitingEvent() = viewModelScope.launch {
        cancelMatchWaitingUseCase().collect {
            when (it) {
                is ApiResponse.Success -> {
                    Timber.d("정상적으로 매칭 취소 완료")
                }

                is ApiResponse.Failure -> {
                    Timber.tag("MatchViewModel").e("${it.code} ${it.errorMessage}")
                }

                ApiResponse.Loading -> {
                    Timber.d("$it")
                }
            }
        }
    }


}
