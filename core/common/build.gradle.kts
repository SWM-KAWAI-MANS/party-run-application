import java.io.File
import java.io.FileInputStream
import java.util.*

plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.hilt")
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val localProperties = Properties()
val localPropertiesFile = File(rootProject.rootDir, "local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "online.partyrun.partyrunapplication.core.common"
    buildTypes {
        getByName("release") {
            buildConfigField("String", "BASE_URL", "\"${localProperties["RELEASE_BASE_URL"] as String?}\"")
        }

        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"${localProperties["DEBUG_BASE_URL"] as String?}\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

secrets {
    defaultPropertiesFileName = "local.properties"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)

}
