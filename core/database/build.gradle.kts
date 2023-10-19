plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.hilt")
    id("nohjunh.android.room")
}

android {
    namespace = "online.partyrun.partyrunapplication.core.database"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(project(":core:testing"))
}