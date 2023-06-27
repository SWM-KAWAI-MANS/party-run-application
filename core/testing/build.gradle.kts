plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
}

android {
    namespace = "online.partyrun.partyrunapplication.testing"
}

dependencies {
    api(libs.androidx.compose.ui.test)
    api(libs.androidx.test.core)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.truth)
    api(libs.androidx.test.ext)
    api(libs.hilt.android.testing)
    api(libs.junit)
    api(libs.mockserver)
    api(libs.mockito.core)
    api(libs.mockito.inline)
    api(libs.kotlinx.coroutines.test)

    debugApi(libs.androidx.compose.ui.testManifest)

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}