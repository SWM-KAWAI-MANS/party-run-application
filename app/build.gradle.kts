plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("com.google.gms.google-services")
    id ("dagger.hilt.android.plugin")
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id ("kotlin-kapt")
}

android {
    namespace = "online.partyrun.partyrunapplication"
    compileSdk = Configurations.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "online.partyrun.partyrunapplication"
        minSdk = Configurations.MIN_SDK_VERSION
        targetSdk = Configurations.TARGET_SDK_VERSION
        versionCode = Configurations.VERSION_CODE
        versionName  = Configurations.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.5"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation (libs.androidx.core.ktx)
    implementation (libs.androidx.core.ktx)
    implementation (platform(libs.kotlin.bom))
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.activity.compose)
    implementation (platform(libs.androidx.compose.bom))
    implementation (libs.androidx.compose.ui.core)
    implementation (libs.androidx.compose.ui.graphics)
    implementation (libs.androidx.compose.ui.tooling.preview)
    implementation (libs.androidx.compose.material3)
    implementation (libs.google.auth)

    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.test.ext)
    androidTestImplementation (libs.androidx.test.espresso.core)
    androidTestImplementation (platform(libs.androidx.compose.bom))
    androidTestImplementation (libs.androidx.compose.ui.test)
    debugImplementation (libs.androidx.compose.ui.tooling)
    debugImplementation (libs.androidx.compose.ui.testManifest)

    // Timber
    implementation (libs.timber)

    // DataStore
    implementation (libs.androidx.dataStore.core)

    // Compose Navigation
    implementation (libs.androidx.navigation.compose)
    // BottomNavigation 및 BottomNavigationItem 구성요소 사용
    implementation (libs.androidx.compose.material.core)

    // Retrofit
    implementation (libs.retrofit.core)
    implementation (libs.retrofit.gson)
    implementation (libs.kotlinx.serialization.json)
    implementation (libs.retrofit.kotlin.serialization)

    // firebase / firestore
    // Import the Firebase BoM
    implementation (platform(libs.firebase.bom))
    // Add the dependency for the Firebase SDK for Google Analytics
    implementation (libs.firebase.analytics)
    implementation (libs.firebase.auth)
    implementation (libs.firebase.storage)

    // Room components
    implementation (libs.room.runtime)
    implementation (libs.room.ktx)
    annotationProcessor (libs.room.compiler)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.kotlinx.coroutines.playservices)

    // Coroutine Lifecycle Scopes
    implementation (libs.androidx.lifecycle.viwemodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.lifecycle.viwemodel.savedstate)

    // Runtime Compose
    implementation (libs.androidx.lifecycle.runtimeCompose)

    // OkHttp
    implementation (libs.okhttp.core)
    implementation (libs.okhttp.logging)

    // splash api
    implementation (libs.androidx.core.splashscreen)

    // Dagger - Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    kapt (libs.hilt.ext.compiler)
    implementation (libs.androidx.hilt.navigation.compose)

    // Coil
    implementation (libs.coil.kt)
    implementation (libs.coil.kt.compose)
    implementation (libs.coil.kt.gif)

    // Lottie
    implementation (libs.lottie.compose)
}
tasks.register("prepareKotlinBuildScriptModel"){}