package online.partyrun.partyrunapplication.feature.running.service

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import online.partyrun.partyrunapplication.core.common.Constants
import online.partyrun.partyrunapplication.core.model.running.GpsData
import java.time.LocalDateTime

@AndroidEntryPoint
class SingleRunningService : BaseRunningService() {

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
        startForeground(Constants.NOTIFICATION_ID, createNotification())
        setLocationCallback()

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
