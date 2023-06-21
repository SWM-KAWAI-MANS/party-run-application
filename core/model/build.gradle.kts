plugins {
    id("nohjunh.android.library")
}

android {
    namespace = "online.partyrun.partyrunapplication.core.model"
}

dependencies {
    implementation(libs.retrofit.gson)
}