plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
}

android {
    namespace = "online.partyrun.partyrunapplication.core.ui"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    testImplementation(libs.junit)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt.compose)
    implementation(libs.lottie.compose)
}