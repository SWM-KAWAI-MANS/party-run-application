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
import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import online.partyrun.partyrunapplication.core.domain.my_page.GetMyPageDataUseCase
import online.partyrun.partyrunapplication.core.model.single.ProfileImageSource
import online.partyrun.partyrunapplication.core.model.single.SingleRunnerStatus
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.util.distanceToCoordinatesMapper
import javax.inject.Inject

@HiltViewModel
class SingleContentViewModel @Inject constructor(
    private val singleRepository: SingleRepository,
    private val getMyPageDataUseCase: GetMyPageDataUseCase
) : ViewModel() {

    companion object {
        const val COUNTDOWN_SECONDS = 5
    }

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

    private fun startSingleRunningService() {
        _singleContentUiState.update { state ->
            state.copy(startRunningService = true)
        }
    }

    private fun updateCountdownState(timeRemaining: Int) {
        if (timeRemaining == 3) { // 3초가 남았을 때 러닝 서비스 시작
            startSingleRunningService()
        }
        _singleContentUiState.update { state ->
            state.copy(timeRemaining = timeRemaining)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _singleContentUiState.value = SingleContentUiState()
        singleRepository.initialize()
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
                delay(1000)  // 1초 대기
                _singleContentUiState.update { state ->
                    val newElapsedSeconds = state.elapsedSecondsTime + 1
                    val formattedTime = formatTime(newElapsedSeconds)
                    state.copy(
                        elapsedSecondsTime = newElapsedSeconds,
                        elapsedFormattedTime = formattedTime
                    )
                }
            }
        }
    }

    fun initSingleState() {
        viewModelScope.launch {
            val userData = getMyPageDataUseCase()
            val userState = SingleRunnerStatus(
                runnerName = userData.name,
                runnerProfile = ProfileImageSource.Url(userData.profile) // 서버에서 관리하는 사용자 프로필 이미지 URL
            )
            val robotState = SingleRunnerStatus(
                runnerName = "파티런 봇",
                runnerProfile = ProfileImageSource.ResourceId(R.drawable.robot)  // 로컬 리소스 ID
            )

            _singleContentUiState.update { state ->
                state.copy(
                    userName = userData.name,
                    userState = userState,
                    robotState = robotState
                )
            }
            startUserMovement()
            startRobotMovement()
        }
    }

    private fun startUserMovement() {
        viewModelScope.launch {
            singleRepository.totalDistance.collect { totalDistance ->
                checkTargetDistanceReached(totalDistance)
                val (updatedUser, formattedDistance) = getUpdatedMovementData(totalDistance)

                _singleContentUiState.update { state ->
                    state.copy(
                        userState = updatedUser,
                        distanceInKm = formattedDistance
                    )
                }
            }
        }
    }

    private fun getUpdatedMovementData(totalDistance: Int): Pair<SingleRunnerStatus, String> {
        val previousState = _singleContentUiState.value
        val previousUser = previousState.userState

        // 거리를 km 단위로 변환하고 소수점 두 자리까지 표현
        val distanceInKm = totalDistance.toDouble() / 1000
        val formattedDistance = String.format("%.2f", distanceInKm)

        val updatedUser = previousUser.copy(
            distance = totalDistance.toDouble()
        )
        return Pair(updatedUser, formattedDistance)
    }

    private fun checkTargetDistanceReached(totalDistance: Int) {
        if (totalDistance >= _singleContentUiState.value.selectedDistance) {
            _singleContentUiState.update { state ->
                state.copy(
                    isFinished = true,
                    screenState = SingleScreenState.Finish
                )
            }
        }
    }

    private fun startRobotMovement() {
        viewModelScope.launch {
            while (true) {
                val previousState = _singleContentUiState.value
                // 현재 경과 시간이 선택된 시간을 넘어갔는지 확인
                if (previousState.elapsedSecondsTime >= previousState.selectedTime) break

                delay(500) // 0.5초마다 움직임

                val robotStep =
                    previousState.selectedDistance.toDouble() / previousState.selectedTime
                val currentElapsedTime = previousState.elapsedSecondsTime
                val previousRobot = previousState.robotState

                val updatedRobot = previousRobot.copy(
                    distance = robotStep * currentElapsedTime
                )

                _singleContentUiState.update { state ->
                    state.copy(
                        robotState = updatedRobot
                    )
                }
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

    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }
}
