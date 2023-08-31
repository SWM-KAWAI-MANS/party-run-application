package online.partyrun.partyrunapplication.feature.running.battle

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import online.partyrun.partyrunapplication.core.common.Constants
import online.partyrun.partyrunapplication.core.common.Constants.ACTION_START_RUNNING
import online.partyrun.partyrunapplication.core.common.Constants.ACTION_STOP_RUNNING
import online.partyrun.partyrunapplication.core.common.Constants.BATTLE_ID_KEY
import online.partyrun.partyrunapplication.core.common.Constants.NOTIFICATION_ID
import online.partyrun.partyrunapplication.core.domain.running.SendRecordDataUseCase
import online.partyrun.partyrunapplication.core.model.running.GpsData
import online.partyrun.partyrunapplication.core.model.running.RecordData
import online.partyrun.partyrunapplication.feature.running.R
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class RunningService : Service() {
    companion object {
        const val LOCATION_UPDATE_INTERVAL_SECONDS = 1L
    }

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var sendRecordDataUseCase: SendRecordDataUseCase

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notification: NotificationCompat.Builder

    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val recordData = mutableListOf<GpsData>() // 1초마다 업데이트한 GPS 데이터를 쌓기 위함

    override fun onCreate() {
        super.onCreate()
        initLocationRequest()
    }

    private fun initLocationRequest() {
        val priority = Priority.PRIORITY_HIGH_ACCURACY
        locationRequest = LocationRequest.Builder(
            priority,
            TimeUnit.SECONDS.toMillis(LOCATION_UPDATE_INTERVAL_SECONDS)
        ).build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_RUNNING -> startRunningService(intent)
            ACTION_STOP_RUNNING -> stopRunningService()
        }
        return START_NOT_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun startRunningService(intent: Intent) {
        val battleId = intent.getStringExtra(BATTLE_ID_KEY) ?: return

        startForeground(NOTIFICATION_ID, createNotification())
        setLocationCallback(battleId)

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun stopRunningService() {
        stopLocationUpdates()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun setLocationCallback(battleId: String) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
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

    private fun addGpsDataToRecordData(location: Location) {
        recordData.add(
            GpsData(
                latitude = location.latitude,
                longitude = location.longitude,
                altitude = location.altitude,
                time = LocalDateTime.now()
            )
        )
    }

    private fun createNotification(): Notification {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val builder = notification
            .setContentTitle(getString(R.string.running_service_title))
            .setContentText(getString(R.string.running_service_content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)

        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        locationCallback.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
