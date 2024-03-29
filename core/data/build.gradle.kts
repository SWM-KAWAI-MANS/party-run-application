plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "online.partyrun.partyrunapplication.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.google.location)

    testImplementation(project(":core:testing"))
}
