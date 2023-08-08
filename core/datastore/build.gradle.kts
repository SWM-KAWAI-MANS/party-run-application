plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.hilt")
    id("com.google.protobuf") version "0.9.3"
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
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
    implementation(libs.timber)
    implementation(libs.protobuf.kotlin.lite)

    testImplementation(project(":core:testing"))
}
