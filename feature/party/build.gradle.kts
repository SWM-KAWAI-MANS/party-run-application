plugins {
    id("nohjunh.android.feature")
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
    id("nohjunh.android.hilt")
}

android {
    namespace = "online.partyrun.partyrunapplication.feature.party"
}

dependencies {
    // Timber
    implementation(libs.timber)
    implementation(libs.okhttp.sse)
    implementation(libs.gson.code)

    implementation(libs.google.accompanist.permission)

    implementation(libs.google.location)
    implementation(project(":feature:match"))
    implementation(project(":feature:running"))
}
