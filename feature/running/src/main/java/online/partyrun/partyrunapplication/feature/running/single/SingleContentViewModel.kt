package online.partyrun.partyrunapplication.feature.running.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import online.partyrun.partyrunapplication.core.common.result.onFailure
import online.partyrun.partyrunapplication.core.common.result.onSuccess
import online.partyrun.partyrunapplication.core.domain.my_page.GetMyPageDataUseCase
import online.partyrun.partyrunapplication.core.domain.running.single.GetRecordDataWithDistanceUseCase
import online.partyrun.partyrunapplication.core.domain.running.single.InitializeSingleUseCase
import online.partyrun.partyrunapplication.core.domain.running.single.SaveSingleIdUseCase
import online.partyrun.partyrunapplication.core.domain.running.single.SendRecordDataWithDistanceUseCase
import online.partyrun.partyrunapplication.core.model.running.RunningTime
import online.partyrun.partyrunapplication.core.model.running.calculateInstantPace
import online.partyrun.partyrunapplication.core.model.single.ProfileImageSource
import online.partyrun.partyrunapplication.core.model.single.SingleRunnerDisplayStatus
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.util.RunningConstants.COUNTDOWN_INTERVAL
import online.partyrun.partyrunapplication.feature.running.util.RunningConstants.COUNTDOWN_SECONDS
import online.partyrun.partyrunapplication.feature.running.util.RunningConstants.ELAPSED_SECONDS_COUNT
import online.partyrun.partyrunapplication.feature.running.util.RunningConstants.ROBOT_MOVEMENT_DELAY
import online.partyrun.partyrunapplication.feature.running.util.distanceToCoordinatesMapper
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SingleContentViewModel @Inject constructor(
    private val sendRecordDataWithDistanceUseCase: SendRecordDataWithDistanceUseCase,
    private val saveSingleIdUseCase: SaveSingleIdUseCase,
    private val getRecordDataWithDistanceUseCase: GetRecordDataWithDistanceUseCase,
    private val initializeSingleUseCase: InitializeSingleUseCase,
    private val getMyPageDataUseCase: GetMyPageDataUseCase
) : ViewModel() {

    private val _singleContentUiState = MutableStateFlow(SingleContentUiState())
    val singleContentUiState: StateFlow<SingleContentUiState> = _singleContentUiState

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = ""
    }

    fun updateSelectedDistanceAndTime(distance: Int, time: Int) {
        _singleContentUiState.value =
            _singleContentUiState.value.copy(
                selectedDistance = distance,
                selectedTime = time
            )
    }

    suspend fun countDownWhenReady() {
        withContext(Dispatchers.Main) {
            countDown()
            changeScreenToRunning()
        }
    }

    private suspend fun countDown() {
        val initialDelay = 700L
        val startCount = COUNTDOWN_SECONDS

        delay(initialDelay)

        for (remainingSeconds in startCount downTo 0) {
            updateCountdownState(remainingSeconds)

            if (remainingSeconds > 0) {
                delay(COUNTDOWN_INTERVAL)
            }
        }
    }

    private fun updateCountdownState(timeRemaining: Int) {
        if (timeRemaining == 5) { // 카운트다운 시작 시 러닝 서비스 활성화
            startSingleRunningService()
        }
        _singleContentUiState.update { state ->
            state.copy(timeRemaining = timeRemaining)
        }
    }

    private fun startSingleRunningService() {
        _singleContentUiState.update { state ->
            state.copy(runningServiceState = RunningServiceState.STARTED)
        }
    }

    fun pauseSingleRunningService(isUserPaused: Boolean) {
        _singleContentUiState.update { state ->
            state.copy(
                runningServiceState = RunningServiceState.PAUSED,
                isUserPaused = isUserPaused // 사용자가 일시정지를 직접 누른 것인지 판단
            )
        }
    }

    fun stopSingleRunningService() {
        _singleContentUiState.update { state ->
            state.copy(runningServiceState = RunningServiceState.STOPPED)
        }
    }

    fun resumeSingleRunningService() {
        _singleContentUiState.update { state ->
            state.copy(
                runningServiceState = RunningServiceState.RESUMED,
                isUserPaused = false // 재시작일 때는 사용자가 직접 누른 것이든, 디바이스 움직임 재시작이든지 상관없이 false로 고정
            )
        }
    }

    private fun stopRunningState() {
        _singleContentUiState.update { state ->
            state.copy(
                isFinished = true,
                screenState = SingleScreenState.Finish
            )
        }
    }

    fun finishRunningProcess() {
        stopSingleRunningService()
        stopRunningState()
    }

    private fun changeScreenToRunning() {
        _singleContentUiState.update { state ->
            state.copy(
                screenState = SingleScreenState.Running
            )
        }
        startOneSecondCounter()
    }

    private fun startOneSecondCounter() {
        viewModelScope.launch {
            while (true) {
                delay(ELAPSED_SECONDS_COUNT)  // ELAPSED_SECONDS_COUNT만큼 대기
                if (_singleContentUiState.value.runningServiceState == RunningServiceState.PAUSED) continue
                secondCountState()
            }
        }
    }

    private fun secondCountState() {
        _singleContentUiState.update { state ->
            state.copy(
                elapsedSecondsTime = state.incrementElapsedSeconds(),
                elapsedFormattedTime = state.formatTime(state.elapsedSecondsTime)
            )
        }
    }

    fun initSingleState() {
        viewModelScope.launch {
            setupInitialStatus()
            startUserMovement()
            startRobotMovement()
        }
    }

    private suspend fun setupInitialStatus() {
        val userData = getMyPageDataUseCase()
        val userStatus = SingleRunnerDisplayStatus(
            runnerName = userData.nickName,
            runnerProfile = ProfileImageSource.Url(userData.profileImage) // 서버에서 관리하는 사용자 프로필 이미지 URL
        )
        val robotStatus = SingleRunnerDisplayStatus(
            runnerName = "파티런 봇",
            runnerProfile = ProfileImageSource.ResourceId(R.drawable.robot)  // 로컬 리소스 ID
        )

        _singleContentUiState.update { state ->
            state.copy(
                userName = userData.nickName,
                userStatus = userStatus,
                robotStatus = robotStatus
            )
        }
    }

    private fun startUserMovement() {
        viewModelScope.launch {
            getRecordDataWithDistanceUseCase().collect { record ->
                val currentDistance = record.records.lastOrNull()?.distance ?: 0.0
                checkTargetDistanceReached(currentDistance)
                val (updatedUser, formattedDistance) =
                    _singleContentUiState.value.getUpdatedMovementData(currentDistance)

                _singleContentUiState.update { state ->
                    state.copy(
                        userStatus = updatedUser,
                        distanceInMeter = currentDistance,
                        distanceInKm = formattedDistance,
                        instantPace = record.calculateInstantPace()
                    )
                }
            }
        }
    }

    private fun checkTargetDistanceReached(totalDistance: Double) {
        if (totalDistance >= _singleContentUiState.value.selectedDistance) {
            sendRecordDataWithDistance {
                finishRunningProcess()
            }
        }
    }

    private fun sendRecordDataWithDistance(
        finishRunningProcess: () -> Unit
    ) {
        viewModelScope.launch {
            val runningTime =
                RunningTime.fromSeconds(_singleContentUiState.value.elapsedSecondsTime)
            sendRecordDataWithDistanceUseCase(runningTime).collect { result ->
                result.onSuccess { data ->
                    saveSingleIdUseCase(data.id)
                    finishRunningProcess()
                }.onFailure { errorMessage, code ->
                    _snackbarMessage.value = "싱글 결과 전송 실패"
                    Timber.e("$code $errorMessage")
                }
            }
        }
    }

    private fun startRobotMovement() {
        viewModelScope.launch {
            while (!_singleContentUiState.value.isElapsedBeyondSelectedTime()) {
                delay(ROBOT_MOVEMENT_DELAY)
                _singleContentUiState.update { state -> state.updatedRobotStatus() }
            }
        }
    }

    // 거리와 좌표를 매핑하는 로직
    fun mapDistanceToCoordinates(
        totalTrackDistance: Double,
        distance: Double,
        trackWidth: Double,
        trackHeight: Double
    ): Pair<Double, Double> {
        val currentDistance =
            distance.coerceAtMost(totalTrackDistance) // distance와 totalTrackDistance 중 더 작은 값을 currentDistance에 저장

        return distanceToCoordinatesMapper(
            totalTrackDistance = totalTrackDistance,
            distance = currentDistance,
            trackWidth = trackWidth,
            trackHeight = trackHeight
        )
    }

    override fun onCleared() {
        super.onCleared()
        _singleContentUiState.value = SingleContentUiState()
        initializeSingleUseCase()
    }
}
