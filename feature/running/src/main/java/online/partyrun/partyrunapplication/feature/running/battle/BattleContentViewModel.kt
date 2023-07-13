package online.partyrun.partyrunapplication.feature.running.battle

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
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
import online.partyrun.partyrunapplication.core.model.battle.BattleEvent
import online.partyrun.partyrunapplication.core.model.battle.BattleState
import online.partyrun.partyrunapplication.core.model.battle.RunnerIds
import online.partyrun.partyrunapplication.core.model.battle.RunnerState
import online.partyrun.partyrunapplication.core.model.running.GpsData
import online.partyrun.partyrunapplication.core.model.running.GpsDatas
import online.partyrun.partyrunapplication.core.network.RealtimeBattleClient
import timber.log.Timber
import java.net.ConnectException
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BattleContentViewModel @Inject constructor(
    private val realtimeBattleClient: RealtimeBattleClient,
    private val fusedLocationProviderClient: FusedLocationProviderClient
): ViewModel() {
    companion object {
        const val LOCATION_UPDATE_INTERVAL_SECONDS = 1L
        const val COUNTDOWN_SECONDS = 5
        const val STATE_SHARE_SUBSCRIPTION_TIMEOUT = 3000L
    }

    private lateinit var locationCallback: LocationCallback
    private val locationRequest: LocationRequest

    private val _battleUiState = MutableStateFlow(BattleUiState())
    val battleUiState: StateFlow<BattleUiState> = _battleUiState

    private val _battleScreenState = MutableStateFlow<BattleScreenState>(BattleScreenState.Ready)
    val battleScreenState: StateFlow<BattleScreenState> = _battleScreenState

    private lateinit var resultResult: StateFlow<BattleEvent>

    private val locationUpdateList = mutableListOf<Location>() // 1초마다 업데이트한 GPS 데이터를 쌓기 위함

    init {
        val priority = Priority.PRIORITY_HIGH_ACCURACY
        locationRequest = LocationRequest.Builder(priority, TimeUnit.SECONDS.toMillis(
            LOCATION_UPDATE_INTERVAL_SECONDS
        )).build()
    }

    fun startBattleStream(
        battleId: String,
        navigateToBattleOnWebSocketError: () -> Unit
    ) {
        resultResult = realtimeBattleClient
            .getBattleStream(battleId = battleId)
            .onStart { onStartBattleStream() }
            .onEach { onEachBattleStream() } // WebSocket에서 새로운 배틀 이벤트가 도착할 때마다 호출
            .catch { t -> handleBattleStreamError(t, navigateToBattleOnWebSocketError) }
            .stateIn(
                scope = viewModelScope, // ViewModel의 수명 주기에 맞게 관리
                started = SharingStarted.WhileSubscribed(STATE_SHARE_SUBSCRIPTION_TIMEOUT),
                initialValue = BattleEvent.BattleDefaultResult()
            )

        collectRunnerResult()
    }

    private fun onStartBattleStream() {
        Timber.tag("BattleViewModel").e("Start")
        _battleUiState.update { state ->
            state.copy(isConnecting = true)
        }
    }

    private fun onEachBattleStream() {
        Timber.tag("BattleViewModel").e("Each")
        _battleUiState.update { state ->
            state.copy(isConnecting = false)
        }
        _battleScreenState.value = BattleScreenState.Running
    }

    private fun handleBattleStreamError(t: Throwable, navigateToBattleOnWebSocketError: () -> Unit) {
        Timber.tag("BattleViewModel").e("Catch")
        _battleUiState.update { state ->
            state.copy(showConnectionError = t is ConnectException)
        }
        if (t is ConnectException) { navigateToBattleOnWebSocketError() }
    }

    private fun collectRunnerResult() {
        viewModelScope.launch {
            resultResult.collect { result ->
                when(result) {
                    is BattleEvent.BattleReadyResult -> {
                        countDownWhenReady(result.startTime)
                    }
                    is BattleEvent.BattleRunnerResult -> {
                        updateBattleStateWithRunnerResult(result)
                    }
                    else -> {} // Handle other cases as needed
                }
            }
        }
    }

    private fun updateBattleStateWithRunnerResult(result: BattleEvent.BattleRunnerResult) {
        val currentBattleState = _battleUiState.value.battleState.battleInfo.toMutableList()
        val existingRunnerState = currentBattleState.find { it.runnerId == result.runnerId }

        val updatedRunnerState = existingRunnerState?.copy(
            distance = result.distance,
            currentRank = 1,  // 적절한 값으로 변경
            currentRound = 1,  // 적절한 값으로 변경
            totalRounds = 1  // 적절한 값으로 변경
        )

        if (updatedRunnerState != null) {
            // 리스트에서 해당 객체의 인덱스를 찾아 수정된 객체로 교체
            val runnerIndex = currentBattleState.indexOf(existingRunnerState)
            currentBattleState[runnerIndex] = updatedRunnerState
        }

        _battleUiState.update { state ->
            state.copy(
                battleState = BattleState(battleInfo = currentBattleState)
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(
        battleId: String
    ) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    locationUpdateList.add(location)
                    if (locationUpdateList.size >= 3) {
                        sendBatchedGPS(battleId)
                    }
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper(),
        )
    }

    fun stopLocationUpdates() {
        if (::locationCallback.isInitialized) { // 초기화 여부 판단
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun sendBatchedGPS(battleId: String) {
        val batchedGPSData = batchGpsData()
        sendGPS(battleId, batchedGPSData) // send the batched GPS data
        locationUpdateList.clear() // clear the location update list
    }

    private fun batchGpsData(): GpsDatas {
        return GpsDatas(
            gpsDatas = locationUpdateList.map { location ->
                GpsData(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    altitude = location.altitude,
                    gpsTime = LocalDateTime.now()
                )
            }
        )
    }

    private fun sendGPS(battleId: String, gpsDatas: GpsDatas) {
        viewModelScope.launch {
            realtimeBattleClient.sendGPS(battleId, gpsDatas)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()

        viewModelScope.launch {
            realtimeBattleClient.close()
        }
    }

    fun initBattleState(runnerIds: RunnerIds) {
        // RunnerIds 객체에서 각 id를 가져와 RunnerState 객체 생성
        val runnerStates = runnerIds.runnerIdData.map { id ->
            RunnerState(
                runnerId = id,
                runnerName = "테스트"
            )
        }
        // 생성된 RunnerState 객체들로 BattleState 객체 생성
        _battleUiState.update { state ->
            state.copy(
                battleState = BattleState(battleInfo = runnerStates)
            )
        }
    }

    private suspend fun countDownWhenReady(battleStartTime: LocalDateTime) {
        withContext(Dispatchers.IO) {
            checkAgainstStartTime(battleStartTime)
        }

        withContext(Dispatchers.Main) {
            countDown()
        }
    }

    private suspend fun checkAgainstStartTime(battleStartTime: LocalDateTime) {
        Timber.tag("checkAgainstStartTime").e("시간 비교 시작")
        var remainingTimeInSeconds: Long

        do {
            val currentTime = LocalDateTime.now()
            val remainingTime = Duration.between(currentTime, battleStartTime)
            remainingTimeInSeconds = remainingTime.seconds
            delay(1000) // Check every 1 second
        } while (remainingTimeInSeconds > COUNTDOWN_SECONDS)
    }

    private suspend fun countDown() {
        for (i in COUNTDOWN_SECONDS  downTo  0) {
            delay(1000)
            _battleUiState.update { state ->
                state.copy(
                    timeRemaining = i
                )
            }
        }
    }
}
