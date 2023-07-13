plugins {
    id("nohjunh.android.library")
}

android {
    namespace = "online.partyrun.partyrunapplication.core.model"
}

dependencies {
    testImplementation(libs.junit)

    implementation(libs.kotlinx.datetime)
    implementation(libs.retrofit.gson)
}