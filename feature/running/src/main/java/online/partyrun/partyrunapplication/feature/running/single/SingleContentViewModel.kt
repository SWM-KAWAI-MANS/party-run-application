package online.partyrun.partyrunapplication.feature.running.single

import android.provider.Settings.Global.getString
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import online.partyrun.partyrunapplication.core.domain.my_page.GetMyPageDataUseCase
import online.partyrun.partyrunapplication.core.model.single.ProfileImageSource
import online.partyrun.partyrunapplication.core.model.single.SingleRunnerStatus
import online.partyrun.partyrunapplication.core.model.single.SingleStatus
import online.partyrun.partyrunapplication.feature.running.R
import online.partyrun.partyrunapplication.feature.running.util.distanceToCoordinatesMapper
import javax.inject.Inject

@HiltViewModel
class SingleContentViewModel @Inject constructor(
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

    suspend fun countDownWhenReady() {
        withContext(Dispatchers.Main) {
            countDown()
            changeScreenToRunning()
        }
    }

    fun updateSelectedDistanceAndTime(distance: Int, time: Int) {
        _singleContentUiState.value =
            _singleContentUiState.value.copy(
                selectedDistance = distance,
                selectedTime = time
            )
    }

    private suspend fun countDown() {
        delay(300) // UI 확인 시간
        for (i in COUNTDOWN_SECONDS downTo 0) {
            delay(1000)
            _singleContentUiState.update { state ->
                state.copy(
                    timeRemaining = i
                )
            }
        }
    }

    private fun changeScreenToRunning() {
        _singleContentUiState.update { state ->
            state.copy(
                screenState = SingleScreenState.Running
            )
        }
    }

    fun initSingleState() {
        viewModelScope.launch {
            val userData = getMyPageDataUseCase()
            val user = SingleRunnerStatus(
                runnerName = userData.name,
                distance = 2000.0,
                runnerProfile = ProfileImageSource.Url(userData.profile) // 서버에서 관리하는 사용자 프로필 이미지 URL
            )
            val robot = SingleRunnerStatus(
                runnerName = "파티런 로봇",
                distance = 1000.0,
                runnerProfile = ProfileImageSource.ResourceId(R.drawable.robot)  // 로컬 리소스 ID
            )

            _singleContentUiState.update { state ->
                state.copy(
                    userName = userData.name,
                    singleState = SingleStatus(singleInfo = listOf(user, robot))
                )
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
        val currentDistance = distance.coerceAtMost(totalTrackDistance)

        return distanceToCoordinatesMapper(
            totalTrackDistance = totalTrackDistance,
            distance = currentDistance,
            trackWidth = trackWidth,
            trackHeight = trackHeight
        )
    }
}
