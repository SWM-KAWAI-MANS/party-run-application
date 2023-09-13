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
import online.partyrun.partyrunapplication.core.model.running.GpsData
import online.partyrun.partyrunapplication.feature.running.single.RunningServiceState
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class SingleRunningService : BaseRunningService() {

    @Inject
    lateinit var singleRepository: SingleRepository

    private var lastLocation: Location? = null // 이전 위치 저장
    private var totalDistance: Int = 0  // 누적 거리 저장
    private var isFirstLocationUpdate = true  // 플래그 초기화
    private lateinit var runningServiceState: RunningServiceState // 상태 변수를 통해 현재 서비스 상태 추적
    private var belowThresholdCount: Int = 0  // 임계값 카운트를 통해 일시정지, 재시작을 구현하기 위함
    private var isUserPaused: Boolean = false // 사용자가 직접 일시정지를 누른 것인지 파악

    companion object {
        private const val PAUSE_THRESHOLD_COUNT = 3
    }

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
                if (shouldSkipLocationUpdate()) return
                processLocationResult(result.lastLocation)
            }
        }
    }

    // onLocationResult의 반환 값이 이전 위치일수도 있는 첫 번째 값은 무시
    private fun shouldSkipLocationUpdate(): Boolean {
        return isFirstLocationUpdate.also { isFirstLocationUpdate = false }
    }

    private fun processLocationResult(location: Location?) {
        location?.let {
            if (handleUserPause(it)) return@let
            if (handleAutomaticPause(it)) return@let
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
        return when {
            isBelowThreshold() -> {
                handleBelowThresholdSituation(currentLocation)
                true
            }

            runningServiceState == RunningServiceState.PAUSED -> {
                resumeRunningService()
                false
            }

            else -> false
        }
    }

    private fun isBelowThreshold() = lastSensorVelocity <= THRESHOLD

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

    override fun addGpsDataToRecordData(location: Location) {
        val gpsData = createGpsData(location)

        // 이전 위치가 있으면 거리를 계산
        lastLocation?.let {
            totalDistance += it.distanceTo(location).roundToInt()
        }
        lastLocation = location

        // Repository에 GPS 데이터 추가
        storeGpsData(gpsData)
    }

    private fun createGpsData(location: Location): GpsData {
        return GpsData(
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.altitude,
            time = LocalDateTime.now()
        )
    }

    private fun storeGpsData(gpsData: GpsData) {
        serviceScope.launch {
            singleRepository.setDistance(totalDistance)
            singleRepository.addGpsData(gpsData)
        }
    }

    private fun shouldPauseService(): Boolean {
        return belowThresholdCount >= PAUSE_THRESHOLD_COUNT && runningServiceState != RunningServiceState.PAUSED
    }
}
