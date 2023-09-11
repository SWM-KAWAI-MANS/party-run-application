package online.partyrun.partyrunapplication.core.common

object Constants {
    const val FIREBASE_GOOGLE_CLIENT_ID = BuildConfig.FIREBASE_GOOGLE_CLIENT_ID

    // App 모듈의 Build.gradle.kts -> BuildType에 따른 BASE_URL 분기 처리
    const val BASE_URL = BuildConfig.BASE_URL

    const val NOTIFICATION_CHANNEL_ID = "Running_notification_id"
    const val NOTIFICATION_CHANNEL_NAME = "Running_location_updates"
    const val NOTIFICATION_ID = 1050

    const val ACTION_START_RUNNING = "StartRunning"
    const val ACTION_STOP_RUNNING = "StopRunning"
    const val ACTION_PAUSE_RUNNING = "PauseRunning"
    const val ACTION_RESUME_RUNNING = "ResumeRunning"
    const val EXTRA_IS_USER_PAUSED = "isUserPaused"

    const val BATTLE_ID_KEY = "BATTLE_ID_KEY"
}
