package online.partyrun.partyrunapplication.feature.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.common.network.MatchSourceManager
import online.partyrun.partyrunapplication.core.domain.MatchResultEventUseCase
import online.partyrun.partyrunapplication.core.domain.WaitingBattleUseCase
import online.partyrun.partyrunapplication.core.domain.WaitingEventUseCase
import online.partyrun.partyrunapplication.core.model.match.MatchResultEventResource
import online.partyrun.partyrunapplication.core.model.match.UserMatchSettings
import online.partyrun.partyrunapplication.core.model.match.WaitingEventResource
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val waitingEventUseCase: WaitingEventUseCase,
    private val matchResultEventUseCase: MatchResultEventUseCase,
    private val waitingBattleUseCase: WaitingBattleUseCase,
    private val MatchSourceManager: MatchSourceManager
): ViewModel() {
    private val _matchUiState = MutableStateFlow(MatchUiState())
    val matchUiState: StateFlow<MatchUiState> = _matchUiState.asStateFlow()

    private val gson = Gson()

    fun closeCustomDialog() {
        _matchUiState.update { state ->
            state.copy(
                isOpen = false
            )
        }
    }

    fun openCustomDialog() {
        _matchUiState.update { state ->
            state.copy(
                isOpen = true
            )
        }
    }

    /**
     * Battle 과정에 대한 전체 프로세스 수행
     * 1. 배틀 매칭 큐에 사용자 등록
     * 2. 해당 큐에 목표한 사용자 수만큼 등록 완료 시
     * 3. 수락/거절 여부 파악
     * 4. 최종 매칭 완료 프로세스 진행
     */
    fun battleMatchProcess(userMatchSettings: UserMatchSettings) = viewModelScope.launch {
        try {
            registerToBattleMatchingQueue(userMatchSettings)
            connectMatchEventSource()
        } catch(e: Exception) {
            // Handle Exception that may occur in the matching process
            Timber.tag("MatchViewModel").e(e, "battleMatchProcess")
            return@launch
        }
    }

    /* REST */
    private suspend fun registerToBattleMatchingQueue(userMatchSettings: UserMatchSettings) =
        waitingBattleUseCase(userMatchSettings).collect() {
            when(it) {
                is ApiResponse.Success -> {
                    _matchUiState.update { state ->
                        state.copy(
                            WaitingRestState = WaitingRestState(
                                message = it.data.message
                            )
                        )
                    }
                }
                is ApiResponse.Failure -> {
                    Timber.tag("SignInViewModel").e("${it.code} ${it.errorMessage}")
                    throw RuntimeException("Failure from API: ${it.errorMessage}")
                }
                ApiResponse.Loading ->  {
                    Timber.tag("SignInViewModel").d("Loading")
                }
            }
        }

    /* SSE */
    private fun handleMatchEvent(data: String) {
        val eventData = gson.fromJson(data, WaitingEventResource::class.java)
        Timber.tag("Event").d("Event Received: isSuccess -: ${eventData.isSuccess}")
        Timber.tag("Event").d("Event Received: message -: ${eventData.message}")
        _matchUiState.update {
            it.copy(
                waitingEventState = WaitingEventState(
                    isSuccess = eventData.isSuccess,
                    message = eventData.message
                )
            )
        }
    }

    private fun handleMatchResultEvent(data: String) {
        val eventData = gson.fromJson(data, MatchResultEventResource::class.java)
        Timber.tag("Event").d("Event Received: status -: ${eventData.status}")
        Timber.tag("Event").d("Event Received: location -: ${eventData.location}")
        _matchUiState.update {
            it.copy(
                matchResultEventState = MatchResultEventState(
                    status = eventData.status,
                    location = eventData.location
                )
            )
        }
    }
    suspend fun connectMatchEventSource() {
        val listener = MatchSourceManager.getMatchEventSourceListener(
            onEvent = ::handleMatchEvent
        )
        MatchSourceManager.connectMatchEventSource(waitingEventUseCase(listener))
    }

    suspend fun connectMatchResultEventSource() {
        val listener = MatchSourceManager.getMatchEventSourceListener(
            onEvent = ::handleMatchResultEvent
        )
        MatchSourceManager.connectMatchResultEventSource(matchResultEventUseCase(listener))
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
