plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.hilt")
    id("kotlinx-serialization")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}


android {
    namespace = "online.partyrun.partyrunapplication.core.network"
    buildFeatures {
        buildConfig = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

secrets {
    defaultPropertiesFileName = "local.properties"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    testImplementation(libs.junit)

    implementation(libs.timber)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.gif)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.dataStore.core)

    // Google
    implementation (libs.google.auth)

    // firebase / firestore
    // Import the Firebase BoM
    implementation (platform(libs.firebase.bom))
    // Add the dependency for the Firebase SDK for Google Analytics
    implementation (libs.firebase.analytics)
    implementation (libs.firebase.auth)
    implementation (libs.firebase.storage)


    api(libs.okhttp.logging)
    api(libs.okhttp.sse)
    api(libs.retrofit.core)
    api(libs.retrofit.gson)
    api(libs.stomp.github)
    api(libs.gson.code)
    api(libs.retrofit.kotlin.serialization)

    testImplementation(project(":core:testing"))
}
