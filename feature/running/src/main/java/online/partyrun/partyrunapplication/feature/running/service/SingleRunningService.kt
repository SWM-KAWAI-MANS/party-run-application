package online.partyrun.partyrunapplication.feature.running.service

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.Constants
import online.partyrun.partyrunapplication.core.common.Constants.EXTRA_IS_USER_PAUSED
import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import online.partyrun.partyrunapplication.core.model.running.GpsDataWithDistance
import online.partyrun.partyrunapplication.feature.running.single.RunningServiceState
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class SingleRunningService : BaseRunningService() {
    companion object {
        private const val MIN_DURATION_SECOND: Double = 0.0
        private const val PAUSE_THRESHOLD_COUNT = 3
        private const val MAXIMUM_RUNNER_SPEED_OF_METERS_PER_SECOND = 12.42
    }

    @Inject
    lateinit var singleRepository: SingleRepository

    private var lastLocation: Location? = null
    private var accumulatedDistance: Double = 0.0
    private var isFirstLocationUpdate = true
    private var isSecondLocationUpdate = true
    private lateinit var runningServiceState: RunningServiceState // 상태 변수를 통해 현재 서비스 상태 추적
    private var belowThresholdCount: Int = 0  // 임계값 카운트를 통해 일시정지, 재시작을 구현하기 위함
    private var isUserPaused: Boolean = false // 사용자가 직접 일시정지를 누른 것인지 파악

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        extractUserPauseStatusFromIntent(intent)
        handleIntentAction(intent)
        return START_NOT_STICKY
    }

    private fun extractUserPauseStatusFromIntent(intent: Intent?) {
        isUserPaused = intent?.getBooleanExtra(EXTRA_IS_USER_PAUSED, false) ?: false
    }

    private fun handleIntentAction(intent: Intent?) {
        when (intent?.action) {
            Constants.ACTION_START_RUNNING -> startRunningService()
            Constants.ACTION_PAUSE_RUNNING -> pauseRunningService()
            Constants.ACTION_RESUME_RUNNING -> resumeRunningService()
            Constants.ACTION_STOP_RUNNING -> stopRunningService()
        }
    }

    private fun startRunningService() {
        initializeRunningServiceState()
        registerSensors()
        setLocationCallback()
        startForeground(Constants.NOTIFICATION_ID, createNotification())
        requestLocationUpdates()
    }

    private fun initializeRunningServiceState() {
        runningServiceState = RunningServiceState.STARTED
        belowThresholdCount = 0  // 시작 시 카운트 초기화
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun pauseRunningService() {
        runningServiceState = RunningServiceState.PAUSED
        broadcastState(RunningServiceState.PAUSED.name)
    }

    private fun resumeRunningService() {
        runningServiceState = RunningServiceState.RESUMED
        isUserPaused = false
        belowThresholdCount = 0  // 재시작 시 카운트 초기화
        broadcastState(RunningServiceState.RESUMED.name)
    }

    override fun stopRunningService() {
        runningServiceState = RunningServiceState.STOPPED
        stopLocationUpdates()
        sensorManager.unregisterListener(sensorEventListener)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun broadcastState(stateName: String) { // View로 전달
        sendBroadcast(Intent(stateName))
    }

    private fun setLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                if (handleFirstLocationUpdate()) return
                if (handleSecondLocationUpdate(result.lastLocation)) return
                processLocationResult(result.lastLocation)
            }
        }
    }

    // onLocationResult의 반환 값이 이전 위치일수도 있는 첫 번째 값은 무시
    private fun handleFirstLocationUpdate(): Boolean {
        return isFirstLocationUpdate.also { isFirstLocationUpdate = false }
    }

    // 두번 째 값은 LastLocation 업데이트
    private fun handleSecondLocationUpdate(location: Location?): Boolean {
        return isSecondLocationUpdate.also {
            if (it) {
                lastLocation = location
                isSecondLocationUpdate = false
            }
        }
    }

    private fun processLocationResult(location: Location?) {
        location?.let {
            if (handleUserPause(it)) return@let
            if (handleAutomaticPause(it)) return@let
            if (!isValidSpeed(it)) return@let
            addGpsDataToRecordData(it)
        }
    }

    private fun handleUserPause(location: Location): Boolean {
        if (isUserPaused) { // 사용자가 직접 일시정지 한 경우, 위치 업데이트만 수행하고 리턴
            lastLocation = location
            return true
        }
        return false
    }

    private fun handleAutomaticPause(currentLocation: Location): Boolean {
        if (lastSensorVelocity <= THRESHOLD) {
            handleBelowThresholdSituation(currentLocation)
            return true
        }
        if (runningServiceState == RunningServiceState.PAUSED) {
            resumeRunningService()
            return false
        }
        return false
    }

    private fun handleBelowThresholdSituation(currentLocation: Location) {
        belowThresholdCount++

        if (runningServiceState == RunningServiceState.PAUSED) {
            lastLocation = currentLocation
            return
        }

        if (shouldPauseService()) {
            pauseRunningService()
        }
    }

    private fun isValidSpeed(currentLocation: Location): Boolean {
        val movingDistanceMeter = calculateDistanceTo(currentLocation)
        val durationSeconds = calculateDurationInSeconds(currentLocation)

        if (durationSeconds == MIN_DURATION_SECOND || MAXIMUM_RUNNER_SPEED_OF_METERS_PER_SECOND < movingDistanceMeter / durationSeconds) {
            return false
        }
        return true
    }

    private fun calculateDistanceTo(currentLocation: Location): Float {
        // 두 위치 사이의 거리 계산
        return lastLocation?.distanceTo(currentLocation) ?: 0f
    }

    private fun calculateDurationInSeconds(currentLocation: Location): Double {
        // 두 위치의 시간 차이를 나노초로 계산
        val timeDifferenceNanos =
            currentLocation.elapsedRealtimeNanos - (lastLocation?.elapsedRealtimeNanos ?: 0L)
        // 초 단위로 변환
        return timeDifferenceNanos / 1_000_000_000.0
    }

    override fun addGpsDataToRecordData(location: Location) {
        val previousLocation = lastLocation ?: return
        lastLocation = location

        accumulatedDistance += previousLocation.distanceTo(location).toDouble()
        val gpsData = createGpsData(location, accumulatedDistance)

        // Repository에 GPS 데이터 추가
        storeGpsData(gpsData)
    }


    private fun createGpsData(location: Location, distance: Double): GpsDataWithDistance {
        return GpsDataWithDistance(
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.altitude,
            time = LocalDateTime.now(),
            distance = distance
        )
    }

    // Repository에 GPS 데이터 추가
    private fun storeGpsData(gpsData: GpsDataWithDistance) {
        serviceScope.launch {
            singleRepository.addGpsData(gpsData)
        }
    }

    private fun shouldPauseService(): Boolean {
        return belowThresholdCount >= PAUSE_THRESHOLD_COUNT && runningServiceState != RunningServiceState.PAUSED
    }
}

