plugins {
    id("nohjunh.android.library")
    kotlin("kapt")
}

android {
    namespace = "online.partyrun.partyrunapplication.core.domain"

}

dependencies {
    testImplementation(libs.junit)

    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
}
