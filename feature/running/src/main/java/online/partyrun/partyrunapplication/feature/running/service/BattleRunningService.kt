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
import online.partyrun.partyrunapplication.core.common.Constants.BATTLE_ID_KEY
import online.partyrun.partyrunapplication.core.common.Constants.NOTIFICATION_ID
import online.partyrun.partyrunapplication.core.domain.running.battle.SendRecordDataUseCase
import online.partyrun.partyrunapplication.core.model.running.GpsData
import online.partyrun.partyrunapplication.core.model.running.RecordData
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BattleRunningService : BaseRunningService() {

    companion object {
        private const val SEND_THRESHOLD = 3
    }

    @Inject
    lateinit var sendRecordDataUseCase: SendRecordDataUseCase

    private var isFirstLocationUpdate = true
    private val recordData = mutableListOf<GpsData>() // 1초마다 업데이트한 GPS 데이터를 쌓기 위함

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START_RUNNING -> startRunningService(intent)
            Constants.ACTION_STOP_RUNNING -> stopRunningService()
        }
        return START_NOT_STICKY
    }

    private fun startRunningService(intent: Intent) {
        val battleId = intent.getStringExtra(BATTLE_ID_KEY) ?: return

        registerSensors()
        setLocationCallback(battleId)
        startForeground(NOTIFICATION_ID, createNotification())
        requestLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
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

    private fun setLocationCallback(battleId: String) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                if (handleFirstLocationUpdate()) return

                processLocationResult(result.lastLocation, battleId)
            }
        }
    }

    // onLocationResult의 반환 값이 이전 위치일수도 있는 첫 번째 값은 무시
    private fun handleFirstLocationUpdate(): Boolean {
        return isFirstLocationUpdate.also { isFirstLocationUpdate = false }
    }

    private fun processLocationResult(location: Location?, battleId: String) {
        location?.let {
            if (shouldSkipDueToVelocity()) return@let

            addGpsDataToRecordData(it)
            if (recordData.size >= SEND_THRESHOLD) {
                sendRecordData(battleId)
            }
        }
    }

    private fun sendRecordData(battleId: String) {
        serviceScope.launch {
            sendRecordDataUseCase(battleId, RecordData(recordData))
            recordData.clear()
        }
    }

    private fun shouldSkipDueToVelocity(): Boolean {
        return lastSensorVelocity <= THRESHOLD
    }

    override fun addGpsDataToRecordData(location: Location) {
        recordData.add(
            GpsData(
                latitude = location.latitude,
                longitude = location.longitude,
                altitude = location.altitude,
                time = LocalDateTime.now()
            )
        )
    }
}
