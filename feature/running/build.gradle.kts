plugins {
    id("nohjunh.android.feature")
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
    id("nohjunh.android.hilt")
}

android {
    namespace = "online.partyrun.partyrunapplication.feature.running"
}

dependencies {
    // Timber
    implementation(libs.timber)
    implementation(libs.gson.code)

    // stomp
    implementation(libs.stomp.github)
    // google Location
    implementation(libs.google.location)

    implementation(project(":core:network"))
}
