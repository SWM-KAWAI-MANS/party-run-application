pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "PartyRunApplication"
include(":app")
include(":core:network")
include(":core:common")
include(":core:designsystem")
include(":core:data")
include(":core:model")
include(":core:domain")
include(":core:datastore")
include(":core:ui")
include(":core:testing")
include(":core:navigation")

include(":feature:splash")
include(":feature:battle")
include(":feature:sign_in")
include(":feature:single")
include(":feature:my_page")
include(":feature:challenge")
include(":feature:match")
include(":feature:running")
include(":feature:running_result")
include(":feature:settings")
include(":feature:party")
