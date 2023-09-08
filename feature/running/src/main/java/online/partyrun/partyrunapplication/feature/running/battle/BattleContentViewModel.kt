package online.partyrun.partyrunapplication.feature.running.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.running.BattleStreamUseCase
import online.partyrun.partyrunapplication.core.domain.running.DisposeSocketResourcesUseCase
import online.partyrun.partyrunapplication.core.domain.running.GetBattleIdUseCase
import online.partyrun.partyrunapplication.core.domain.running.GetBattleStatusUseCase
import online.partyrun.partyrunapplication.core.domain.running.GetUserIdUseCase
import online.partyrun.partyrunapplication.core.domain.running.SaveBattleIdUseCase
import online.partyrun.partyrunapplication.core.model.battle.BattleStatus
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import online.partyrun.partyrunapplication.feature.running.util.distanceToCoordinatesMapper
import timber.log.Timber
import java.net.ConnectException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class BattleContentViewModel @Inject constructor(
    private val battleStreamUseCase: BattleStreamUseCase,
    private val getBattleIdUseCase: GetBattleIdUseCase,
    private val getBattleStatusUseCase: GetBattleStatusUseCase,
    private val saveBattleIdUseCase: SaveBattleIdUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val disposeSocketResourcesUseCase: DisposeSocketResourcesUseCase,
) : ViewModel() {
    companion object {
        const val COUNTDOWN_SECONDS = 5
        const val STATE_SHARE_SUBSCRIPTION_TIMEOUT = 3000L
    }

    private lateinit var userId: String
    private var isFirstBattleStreamCall = true // onEach 분기를 위한 boolean

    private val _battleContentUiState = MutableStateFlow(BattleContentUiState())
    val battleContentUiState: StateFlow<BattleContentUiState> = _battleContentUiState

    private val _battleId = MutableStateFlow<String?>(null)
    val battleId: StateFlow<String?> = _battleId

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    private lateinit var runnersState: StateFlow<BattleEvent>

    init {
        getBattleId()
        getUserId()
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun updateSelectedDistance(distance: Int) {
        _battleContentUiState.value = _battleContentUiState.value.copy(selectedDistance = distance)
    }

    private fun getUserId() = viewModelScope.launch {
        userId = getUserIdUseCase()
        _battleContentUiState.update { state ->
            state.copy(
                userId = userId
            )
        }
    }

    private fun getBattleId() = viewModelScope.launch {
        getBattleIdUseCase().collect { result ->
            result.onSuccess { data ->
                saveBattleIdUseCase(data.id) // DataStore에 BattleId 저장
                _battleId.value = data.id
            }.onFailure { errorMessage, code ->
                _snackbarMessage.value = "배틀 정보를 가져올 수 없습니다."
                Timber.e("$code $errorMessage")
            }
        }
    }

    suspend fun startBattleStream(
        battleId: String,
        navigateToBattleOnWebSocketError: () -> Unit
    ) {
        runnersState = battleStreamUseCase
            .getBattleStream(battleId = battleId)
            .onStart { onStartBattleStream() }
            .onEach { onWebSocketConnected() } // WebSocket에서 새로운 배틀 이벤트가 도착할 때마다 호출
            .catch { t -> handleBattleStreamError(t, navigateToBattleOnWebSocketError) }
            .stateIn(
                scope = viewModelScope, // ViewModel의 수명 주기에 맞게 관리
                started = SharingStarted.WhileSubscribed(STATE_SHARE_SUBSCRIPTION_TIMEOUT),
                initialValue = BattleEvent.BattleDefault()
            )
        collectRunnersState()
    }

    private fun onWebSocketConnected() {
        if (isFirstBattleStreamCall) {
            _battleContentUiState.update { state ->
                state.copy(
                    isConnecting = false // false = 연결 완료
                )
            }
        }
        isFirstBattleStreamCall = false
    }

    private suspend fun collectRunnersState() {
        viewModelScope.launch {
            runnersState.collect {
                when (it) {
                    is BattleEvent.BattleStart -> { // 대결 시작 시간 처리
                        countDownWhenReady(it.startTime)
                    }

                    is BattleEvent.BattleRunning -> { // 달리는 유저들의 거리 상태 처리
                        updateBattleStateWithRunnerResult(it)
                    }

                    is BattleEvent.BattleFinished -> { // 대결 종료 상태 처리
                        handleBattleFinished(it.runnerId)
                    }

                    else -> {
                        Timber.e("다른 Type의 BattleEvent 수신", it)
                    }
                }
            }
        }
    }

    private fun onStartBattleStream() {
        _battleContentUiState.update { state ->
            state.copy(
                isConnecting = true
            )
        }
    }

    private fun handleBattleStreamError(
        t: Throwable,
        navigateToBattleOnWebSocketError: () -> Unit
    ) {
        _battleContentUiState.update { state ->
            state.copy(showConnectionError = t is ConnectException)
        }
        if (t is ConnectException) {
            navigateToBattleOnWebSocketError()
        }
    }

    private fun updateBattleStateWithRunnerResult(result: BattleEvent.BattleRunning) {
        val currentBattleState = _battleContentUiState.value.battleState.battleInfo.toMutableList()
        val existingRunnerState = currentBattleState.find { it.runnerId == result.runnerId }

        val updatedRunnerState = existingRunnerState?.copy(
            distance = result.distance,
            currentRound = 1,  // 적절한 값으로 변경
            totalRounds = 1  // 적절한 값으로 변경
        )

        if (updatedRunnerState != null) {
            // 리스트에서 해당 객체의 인덱스를 찾아 수정된 객체로 교체
            val runnerIndex = currentBattleState.indexOf(existingRunnerState)
            currentBattleState[runnerIndex] = updatedRunnerState
        }

        // 거리에 따라 러너들을 정렬
        val sortedRunners = currentBattleState.sortedByDescending { it.distance }

        // 순위 업데이트
        val rankedRunners = sortedRunners.mapIndexed { index, runner ->
            runner.copy(currentRank = index + 1)
        }

        // 업데이트된 상태로 저장
        _battleContentUiState.update { state ->
            state.copy(
                battleState = BattleStatus(battleInfo = rankedRunners)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.launch {
            battleStreamUseCase.close()
        }
    }

    fun initBattleState() {
        viewModelScope.launch {
            val battleData = getBattleStatusUseCase()

            // 생성된 RunnerState 객체들로 BattleState 객체 생성
            _battleContentUiState.update { state ->
                state.copy(
                    battleState = battleData
                )
            }
        }
    }

    private suspend fun countDownWhenReady(battleStartTime: LocalDateTime) {
        withContext(Dispatchers.IO) {
            checkAgainstStartTime(battleStartTime)
        }

        withContext(Dispatchers.Main) {
            changeScreenToRunning()
            countDown()
        }
    }

    private fun changeScreenToRunning() {
        _battleContentUiState.update { state ->
            state.copy(
                screenState = BattleScreenState.Running
            )
        }
    }

    private suspend fun checkAgainstStartTime(battleStartTime: LocalDateTime) {
        var remainingTimeInSeconds: Long

        do {
            val currentTime = LocalDateTime.now()
            val remainingTime = ChronoUnit.SECONDS.between(currentTime, battleStartTime)
            remainingTimeInSeconds = remainingTime
            delay(1000) // Check every 1 second
        } while (remainingTimeInSeconds > COUNTDOWN_SECONDS)
    }

    private suspend fun countDown() {
        val initialDelay = 300L
        val countDownInterval = 1000L
        val startCount = COUNTDOWN_SECONDS

        delay(initialDelay)

        for (remainingSeconds in startCount downTo 0) {
            updateCountdownState(remainingSeconds)

            if (remainingSeconds > 0) {
                delay(countDownInterval)
            }
        }
    }

    private fun startBattleRunningService() {
        _battleContentUiState.update { state ->
            state.copy(startRunningService = true)
        }
    }

    private fun updateCountdownState(timeRemaining: Int) {
        if (timeRemaining == 3) { // 3초가 남았을 때 러닝 서비스 시작
            startBattleRunningService()
        }
        _battleContentUiState.update { state ->
            state.copy(timeRemaining = timeRemaining)
        }
    }

    fun disposeSocketResources() = viewModelScope.launch {
        Timber.tag("disposeSocketResources").d("웹소켓 리소스 해제")
        disposeSocketResourcesUseCase()
    }

    private fun handleBattleFinished(runnerId: String) {
        if (runnerId == userId) {  // 내 아이디와 비교하는 작업 수행
            _battleContentUiState.update { state ->
                state.copy(
                    isFinished = true,
                    screenState = BattleScreenState.Finish
                )
            }
            Timber.tag("handleBattleFinished").d("웹소켓 리소스 해제")
            disposeSocketResources() // 웹소켓 리소스 해제
        }
    }

    // 거리와 좌표를 매핑하는 로직
    fun mapDistanceToCoordinates(
        totalTrackDistance: Double,
        distance: Double,
        trackWidth: Double,
        trackHeight: Double
    ): Pair<Double, Double> {
        val currentDistance = distance.coerceAtMost(totalTrackDistance)

        return distanceToCoordinatesMapper(
            totalTrackDistance = totalTrackDistance,
            distance = currentDistance,
            trackWidth = trackWidth,
            trackHeight = trackHeight
        )
    }

    fun getRunnerDistance(): String {
        val runnerStatus =
            _battleContentUiState.value.battleState.battleInfo.find { it.runnerId == userId }
        val distance = runnerStatus?.distance?.toInt() ?: 0
        return "${distance}m"
    }
}
