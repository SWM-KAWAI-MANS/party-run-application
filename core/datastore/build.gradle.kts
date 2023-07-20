plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.hilt")
}

android {
    namespace = "online.partyrun.partyrunapplication.core.datastore"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.androidx.dataStore.core)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(project(":core:testing"))
}