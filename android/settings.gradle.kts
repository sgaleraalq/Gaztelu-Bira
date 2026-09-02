import org.gradle.api.initialization.resolve.RepositoriesMode.FAIL_ON_PROJECT_REPOS

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Gaztelu Bira"

include(":gaztelu_bira")

/**
 * Android Only
 */
include(":core:common")
include(":core:data")
include(":core:designsystem")
include(":core:domain")
include(":core:navigation")
include(":core:preview")
include(":core:screens")

/**
 * Multiplatform
 */
include(":common:ui")
project(":common").projectDir = File(settingsDir, "../common")
project(":common:ui").projectDir = File(settingsDir, "../common/ui")
