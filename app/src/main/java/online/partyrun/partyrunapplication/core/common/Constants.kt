package online.partyrun.partyrunapplication.core.common

import online.partyrun.partyrunapplication.BuildConfig

object Constants {
    const val FB_GOOGLE_WEB_CLIENT_ID = BuildConfig.FB_GOOGLE_WEB_CLIENT_ID
    // App 모듈의 Build.gradle.kts -> BuildType에 따른 BASE_URL 분기 처리
    const val BASE_URL = BuildConfig.BASE_URL
}