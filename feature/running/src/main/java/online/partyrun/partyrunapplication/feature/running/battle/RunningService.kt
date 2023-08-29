package online.partyrun.partyrunapplication.feature.running.battle

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import online.partyrun.partyrunapplication.core.common.Constants.NOTIFICATION_CHANNEL_ID
import online.partyrun.partyrunapplication.core.common.Constants.NOTIFICATION_ID
import online.partyrun.partyrunapplication.feature.running.R

class RunningService : Service() {

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

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun createNotification(): Notification {
        val title = "파티런"
        val content = "현재 러닝 진행 중입니다."

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_partyrun_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        return builder.build()
    }
}
