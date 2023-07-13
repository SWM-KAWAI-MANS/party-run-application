plugins {
    id("nohjunh.android.library")
    id("nohjunh.android.library.compose")
}

android {
    namespace = "online.partyrun.partyrunapplication.core.navigation"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    
    // feature modules
    implementation(project(":feature:sign_in"))
    implementation(project(":feature:my_page"))
    implementation(project(":feature:battle"))
    implementation(project(":feature:single"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:match"))
    implementation(project(":feature:running"))
    implementation(project(":feature:challenge"))

    // BottomNavigation 및 BottomNavigationItem 구성요소 사용
    implementation (libs.androidx.compose.material.core)

    // Material 3 사용
    implementation (platform(libs.androidx.compose.bom))
    implementation (libs.androidx.compose.material3)

    implementation(libs.kotlinx.coroutines.android)
    api(libs.androidx.navigation.compose)

    testImplementation(project(":core:testing"))
}
