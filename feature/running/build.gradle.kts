plugins {
    id("nohjunh.android.feature")
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
    id("nohjunh.android.hilt")
    id("org.jetbrains.kotlin.android")
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

    implementation(libs.google.accompanist.permission)

    implementation(project(":core:network"))
    implementation(project(":feature:running_result"))
}
