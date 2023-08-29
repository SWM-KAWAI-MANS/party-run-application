package online.partyrun.partyrunapplication.feature.running.battle

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import online.partyrun.partyrunapplication.core.common.Constants
import online.partyrun.partyrunapplication.core.common.Constants.NOTIFICATION_ID
import javax.inject.Inject

@AndroidEntryPoint
class RunningService : Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notification: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "StartRunning" -> {
                startForeground(NOTIFICATION_ID, createNotification())
            }

            "StopRunning" -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val title = "파티런"
        val content = "현재 러닝 진행 중입니다."

        val builder = notification
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)

        return builder.build()
    }

}
