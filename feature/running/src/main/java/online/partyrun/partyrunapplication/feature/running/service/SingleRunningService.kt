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
import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import online.partyrun.partyrunapplication.core.model.running.GpsData
import online.partyrun.partyrunapplication.feature.running.single.RunningServiceState
import timber.log.Timber
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isUserPaused = intent?.getBooleanExtra("isUserPaused", false) ?: false
        handleIntentAction(intent)
        return START_NOT_STICKY
    }

    private fun handleIntentAction(intent: Intent?) {
        when (intent?.action) {
            Constants.ACTION_START_RUNNING -> startRunningService()
            Constants.ACTION_PAUSE_RUNNING -> pauseRunningService()
            Constants.ACTION_RESUME_RUNNING -> resumeRunningService()
            Constants.ACTION_STOP_RUNNING -> stopRunningService()
        }
    }

    @SuppressLint("MissingPermission")
    fun startRunningService() {
        runningServiceState = RunningServiceState.STARTED
        belowThresholdCount = 0  // 시작 시 카운트 초기화
        registerSensors()
        setLocationCallback()
        startForeground(Constants.NOTIFICATION_ID, createNotification())
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun pauseRunningService() {
        runningServiceState = RunningServiceState.PAUSED
        sendBroadcast(Intent(RunningServiceState.PAUSED.name)) // View로 전달
    }

    private fun resumeRunningService() {
        runningServiceState = RunningServiceState.RESUMED
        isUserPaused = false
        belowThresholdCount = 0  // 재시작 시 카운트 초기화
        sendBroadcast(Intent(RunningServiceState.RESUMED.name)) // View로 전달
    }

    override fun stopRunningService() {
        runningServiceState = RunningServiceState.STOPPED
        stopLocationUpdates()
        sensorManager.unregisterListener(sensorEventListener)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun setLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                // onLocationResult의 반환 값이 이전 위치일수도 있는 첫 번째 값은 무시
                if (isFirstLocationUpdate) {
                    isFirstLocationUpdate = false  // 플래그 업데이트
                    return
                }
                handleLocationResult(result.lastLocation)
            }
        }
    }

    private fun handleLocationResult(location: Location?) {
        location?.let {
            if (isUserPaused) { // 사용자가 직접 일시정지 한 경우, 위치 업데이트만 수행하고 리턴
                Timber.tag("유저").e("사용자 일시정지")
                lastLocation = it
                return@let
            }

            if (lastSensorVelocity <= THRESHOLD) {
                belowThresholdCount++
                if (runningServiceState == RunningServiceState.PAUSED) {
                    Timber.tag("자동").e("자동 일시정지")
                    lastLocation = it // 일시정지 상태일 때는 마지막 위치만 업데이트
                    return@let
                }
                if (shouldPauseService()) {
                    pauseRunningService()
                }
                return@let
            }
            if (runningServiceState == RunningServiceState.PAUSED) {
                resumeRunningService()
            }
            addGpsDataToRecordData(it)
        }
    }

    override fun addGpsDataToRecordData(location: Location) {
        Timber.tag("GPS").e("GPS 데이터 수집")
        val gpsData = GpsData(
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.altitude,
            time = LocalDateTime.now()
        )

        // 이전 위치가 있으면 거리를 계산
        lastLocation?.let {
            totalDistance += it.distanceTo(location).roundToInt()
        }
        lastLocation = location

        // Repository에 GPS 데이터 추가
        serviceScope.launch {
            singleRepository.setDistance(totalDistance)
            singleRepository.addGpsData(gpsData)
        }
    }

    private fun shouldPauseService(): Boolean {
        return belowThresholdCount >= 3 && runningServiceState != RunningServiceState.PAUSED
    }
}
