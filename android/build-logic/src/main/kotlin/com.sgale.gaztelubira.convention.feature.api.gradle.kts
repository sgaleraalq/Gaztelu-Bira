import common.Constants.COMPOSE_LIFECYCLE
import common.Constants.KOTLIN_SERIALIZATION
import common.Constants.NAV3_RUNTIME
import common.Constants.NAV3_UI
import common.Constants.PLUGIN_KOTLIN_COMPOSE
import common.Constants.PLUGIN_KOTLIN_SERIALIZATION
import common.ProjectConstants.PROJECT_NAVIGATION
import extensions.addDependencies
import extensions.addProjectDependencies
import extensions.getPluginId
import extensions.getVersionCatalog

val libs: VersionCatalog = getVersionCatalog()

plugins {
    id("com.sgale.gaztelubira.convention.library")
}

with(pluginManager) {
    apply(libs.getPluginId(PLUGIN_KOTLIN_COMPOSE))
    apply(libs.getPluginId(PLUGIN_KOTLIN_SERIALIZATION))
}

val featureProjectDependencies = listOf(
    PROJECT_NAVIGATION
)

val apiCoreDependencies = listOf(
    COMPOSE_LIFECYCLE,
    KOTLIN_SERIALIZATION,
    NAV3_RUNTIME,
    NAV3_UI
)

dependencies {
    addProjectDependencies(featureProjectDependencies)
    addDependencies(libs, apiCoreDependencies)
}
