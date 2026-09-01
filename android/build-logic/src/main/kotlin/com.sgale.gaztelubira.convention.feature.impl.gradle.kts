import com.android.build.api.dsl.LibraryExtension
import common.Constants.NAV3_RUNTIME
import common.ProjectConstants.PROJECT_COMMON
import common.ProjectConstants.PROJECT_DESIGN_SYSTEM
import common.ProjectConstants.PROJECT_DOMAIN
import common.ProjectConstants.PROJECT_NAVIGATION
import common.ProjectConstants.PROJECT_PREVIEW
import extensions.addDependencies
import extensions.addProjectDependencies
import extensions.getVersionCatalog

plugins {
    id("com.sgale.gaztelubira.convention.library")
    id("com.sgale.gaztelubira.convention.compose")
    id("com.sgale.gaztelubira.convention.hilt")
}

/**
 * Each feature gets its own BuildConfig so debug-only UI can gate on its own `DEBUG` flag
 * instead of borrowing another module's generated class.
 */
extensions.configure<LibraryExtension> {
    buildFeatures {
        buildConfig = true
    }
}

val featureProjectDependencies = listOf(
    PROJECT_COMMON,
    PROJECT_DESIGN_SYSTEM,
    PROJECT_DOMAIN,
    PROJECT_NAVIGATION,
    PROJECT_PREVIEW
)

val externalDependencies = listOf(
    NAV3_RUNTIME
)

val libs = getVersionCatalog()

dependencies {
    addProjectDependencies(featureProjectDependencies)
    addDependencies(libs, externalDependencies)
}
