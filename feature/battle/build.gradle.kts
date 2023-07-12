plugins {
    id("nohjunh.android.feature")
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
    id("nohjunh.android.hilt")
}

android {
    namespace = "online.partyrun.partyrunapplication.feature.battle"
}

dependencies {
    // Timber
    implementation(libs.timber)
    implementation(libs.okhttp.sse)
    implementation(libs.gson.code)

    implementation(libs.exo.player)

    implementation(project(":feature:match"))
}