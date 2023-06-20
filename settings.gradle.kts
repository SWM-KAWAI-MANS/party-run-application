pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
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
include(":core:navigation")

include(":feature:splash")
include(":feature:battle")
include(":feature:sign_in")
include(":feature:single")
include(":feature:my_page")
include(":feature:challenge")
