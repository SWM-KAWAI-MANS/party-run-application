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
import online.partyrun.partyrunapplication.core.domain.running.SendRecordDataUseCase
import online.partyrun.partyrunapplication.core.model.running.GpsData
import online.partyrun.partyrunapplication.core.model.running.RecordData
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BattleRunningService : BaseRunningService() {

    @Inject
    lateinit var sendRecordDataUseCase: SendRecordDataUseCase

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START_RUNNING -> startRunningService(intent)
            Constants.ACTION_STOP_RUNNING -> stopRunningService()
        }
        return START_NOT_STICKY
    }

    @SuppressLint("MissingPermission")
    fun startRunningService(intent: Intent) {
        val battleId = intent.getStringExtra(BATTLE_ID_KEY) ?: return

        registerSensors()
        startForeground(NOTIFICATION_ID, createNotification())
        setLocationCallback(battleId)

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
                result.lastLocation?.let { location ->
                    if (lastSensorVelocity <= THRESHOLD) {
                        return@let
                    }
                    addGpsDataToRecordData(location)
                    if (recordData.size >= 3) {
                        serviceScope.launch {
                            sendRecordDataUseCase(battleId, RecordData(recordData))
                            recordData.clear() // clear the location update list
                        }
                    }
                }
            }
        }
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
