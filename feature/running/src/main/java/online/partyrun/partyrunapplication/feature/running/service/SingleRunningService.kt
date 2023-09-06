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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START_RUNNING -> startRunningService()
            Constants.ACTION_STOP_RUNNING -> stopRunningService()
        }
        return START_NOT_STICKY
    }

    @SuppressLint("MissingPermission")
    fun startRunningService() {
        registerSensors()
        setLocationCallback()
        startForeground(Constants.NOTIFICATION_ID, createNotification())

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    override fun stopRunningService() {
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

                result.lastLocation?.let { location ->
                    if (lastSensorVelocity <= THRESHOLD) {
                        return@let
                    }
                    addGpsDataToRecordData(location)
                }
            }
        }
    }

    override fun addGpsDataToRecordData(location: Location) {
        val gpsData = GpsData(
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.altitude,
            time = LocalDateTime.now()
        )

        // 이전 위치가 있으면 거리를 계산
        lastLocation?.let {
            val distance = it.distanceTo(location) // 거리를 미터 단위로 계산
            val roundedDistance = distance.roundToInt()  // 소수점을 반올림
            totalDistance += roundedDistance  // 누적 거리에 더함
        }

        // 현재 위치를 이전 위치로 저장
        lastLocation = location

        // Repository에 GPS 데이터 추가
        serviceScope.launch {
            singleRepository.setDistance(totalDistance)
            singleRepository.addGpsData(gpsData)
        }
    }
}
