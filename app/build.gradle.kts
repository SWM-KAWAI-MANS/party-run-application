import java.io.File
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("nohjunh.android.application")
    id("nohjunh.android.application.compose")
    id("nohjunh.android.hilt")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("keystore.properties")
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

val localProperties = Properties()
val localPropertiesFile = File(rootProject.rootDir, "local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "online.partyrun.partyrunapplication"
    compileSdk = Configurations.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "online.partyrun.partyrunapplication"
        minSdk = Configurations.MIN_SDK_VERSION
        targetSdk = Configurations.TARGET_SDK_VERSION
        versionCode = Configurations.VERSION_CODE
        versionName = Configurations.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    signingConfigs {
        create("config") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("config")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${localProperties["RELEASE_BASE_URL"] as String?}\""
            )
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${localProperties["DEBUG_BASE_URL"] as String?}\""
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    // core modules
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:testing"))
    implementation(project(":core:ui"))

    // feature modules
    implementation(project(":feature:battle"))
    implementation(project(":feature:challenge"))
    implementation(project(":feature:match"))
    implementation(project(":feature:my_page"))
    implementation(project(":feature:party"))
    implementation(project(":feature:running"))
    implementation(project(":feature:running_result"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:sign_in"))
    implementation(project(":feature:single"))
    implementation(project(":feature:splash"))

    // androidx
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.core)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    // Google
    implementation(libs.google.auth)

    // Timber
    implementation(libs.timber)

    // firebase / firestore
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    // Add the dependency for the Firebase SDK for Google Analytics
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.playservices)

    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viwemodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viwemodel.savedstate)

    // Coil
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.gif)

    // hilt ext
    kapt(libs.hilt.ext.compiler)

    // Lottie
    implementation(libs.lottie.compose)

    // In-App-Update
    implementation(libs.google.`in`.app.update)
    implementation(libs.google.`in`.app.update.ktx)
}
tasks.register("prepareKotlinBuildScriptModel") {}
