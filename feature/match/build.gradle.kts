plugins {
    id("nohjunh.android.feature")
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
    id("nohjunh.android.hilt")
}

android {
    namespace = "online.partyrun.partyrunapplication.feature.match"
}

dependencies {
    // Timber
    implementation(libs.timber)
    implementation(libs.okhttp.sse)
    implementation("com.google.code.gson:gson:2.10.1")
    

}