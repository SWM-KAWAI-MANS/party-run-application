plugins {
    id("nohjunh.android.feature")
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
}

android {
    namespace = "online.partyrun.partyrunapplication.feature.splash"
}

dependencies {
    // Timber
    implementation (libs.timber)

}
