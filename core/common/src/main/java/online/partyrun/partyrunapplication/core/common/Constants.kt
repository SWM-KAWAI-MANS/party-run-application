package online.partyrun.partyrunapplication.core.common

object Constants {
    const val FIREBASE_GOOGLE_CLIENT_ID = BuildConfig.FIREBASE_GOOGLE_CLIENT_ID
    // App 모듈의 Build.gradle.kts -> BuildType에 따른 BASE_URL 분기 처리
    const val BASE_URL = BuildConfig.BASE_URL
}
