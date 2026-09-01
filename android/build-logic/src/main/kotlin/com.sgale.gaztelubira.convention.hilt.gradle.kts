import common.Constants.HILT
import common.Constants.HILT_COMPILER
import common.Constants.PLUGIN_HILT
import common.Constants.PLUGIN_KSP
import extensions.addDependencies
import extensions.addKspDependency
import extensions.getPluginId
import extensions.getVersionCatalog

val libs = getVersionCatalog()

pluginManager.apply(libs.getPluginId(PLUGIN_HILT))
pluginManager.apply(libs.getPluginId(PLUGIN_KSP))

val hiltDependencies = listOf(
    HILT
)

dependencies {
    addDependencies(libs, hiltDependencies)
    addKspDependency(libs, HILT_COMPILER)
}
