plugins {
    id("nohjunh.android.feature")
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
    id("nohjunh.android.hilt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "online.partyrun.partyrunapplication.feature.running_result"
}

dependencies {
    // Timber
    implementation(libs.timber)

    // Google Maps Compose
    implementation(libs.google.maps.compose)

    // Google Maps
    implementation(libs.google.maps)
    implementation(libs.google.location)

    // Maps SDK for Android
    implementation(libs.google.maps.sdk)
    // SDK Android Utility Library
    implementation(libs.google.maps.utility)

}