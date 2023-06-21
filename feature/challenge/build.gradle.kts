plugins {
    id("nohjunh.android.feature")
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
    id("nohjunh.android.hilt")
}

android {
    namespace = "online.partyrun.partyrunapplication.feature.challenge"
}

dependencies {

    // Timber
    implementation (libs.timber)

}