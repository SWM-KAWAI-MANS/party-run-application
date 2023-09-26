package online.partyrun.partyrunapplication.feature.running.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import online.partyrun.partyrunapplication.core.common.Constants
import online.partyrun.partyrunapplication.feature.running.R
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.sqrt

@AndroidEntryPoint
abstract class BaseRunningService : Service() {

    companion object {
        const val LOCATION_UPDATE_INTERVAL_SECONDS = 1L
        const val THRESHOLD = 0.08
    }

    @Inject
    lateinit var sensorManager: SensorManager

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notification: NotificationCompat.Builder

    private val job = Job()
    val serviceScope = CoroutineScope(Dispatchers.IO + job)
    private var lastUpdateTime: Long = 0

    // 이전 위치와 속도를 저장하는 변수
    private var previousX = 0.0
    private var previousY = 0.0
    var lastSensorVelocity: Double = 0.0

    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

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

    abstract fun stopRunningService()
    abstract fun addGpsDataToRecordData(location: Location)

    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // 현재 사용하지 않는 메소드지만 반드시 오버라이드해야 하는 추상 메서드
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                when (it.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE -> handleSensorEvent(it)
                }
            }
        }
    }

    private fun handleSensorEvent(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastUpdateTime) >= 1000) {
            val xValue = event.values[0].toDouble()
            val yValue = event.values[1].toDouble()

            lastSensorVelocity = calculateRoundedVelocity(xValue, yValue)
            lastUpdateTime = currentTime
        }
    }

    private fun calculateRoundedVelocity(x: Double, y: Double): Double {
        val rawVelocity = calculateVelocity(x, y)
        return roundToTwoDecimalPlaces(rawVelocity)
    }

    private fun roundToTwoDecimalPlaces(value: Double): Double {
        return ceil(value * 100) / 100
    }

    fun registerSensors() {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        sensorManager.registerListener(
            sensorEventListener,
            gyroscope,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    /*
     * 새로운 위치를 사용하여 속도 계산
     */
    private fun calculateVelocity(newX: Double, newY: Double): Double {
        val deltaX = newX - previousX
        val deltaY = newY - previousY
        val distance = sqrt((deltaX * deltaX + deltaY * deltaY))
        val velocity = distance / LOCATION_UPDATE_INTERVAL_SECONDS

        previousX = newX
        previousY = newY

        return velocity
    }

    fun createNotification(): Notification {
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

    fun stopLocationUpdates() {
        locationCallback.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sensorEventListener) // 센서 해제
        stopLocationUpdates()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
