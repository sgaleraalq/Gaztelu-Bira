import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import common.Constants.ANDROID
import common.Constants.ANDROID_APPLICATION
import common.Constants.ANDROID_LIBRARY
import common.Constants.COMPOSE_ACTIVITY
import common.Constants.COMPOSE_BOM
import common.Constants.COMPOSE_FOUNDATION
import common.Constants.COMPOSE_LIFECYCLE
import common.Constants.COMPOSE_MATERIAL3
import common.Constants.COMPOSE_MATERIAL_ICONS
import common.Constants.COMPOSE_UI
import common.Constants.COMPOSE_UI_TOOLING
import common.Constants.COMPOSE_UI_TOOLING_PREVIEW
import common.Constants.PLUGIN_KOTLIN_COMPOSE
import extensions.addBOM
import extensions.addDebugDependencies
import extensions.addDependencies
import extensions.getPluginId
import extensions.getVersionCatalog
import extensions.projectLibs

private val implementationDependencies = listOf(
    COMPOSE_FOUNDATION,
    COMPOSE_LIFECYCLE,
    COMPOSE_MATERIAL3,
    COMPOSE_MATERIAL_ICONS,
    COMPOSE_UI,
    COMPOSE_UI_TOOLING_PREVIEW
)

private val debugDependencies = listOf(
    COMPOSE_UI_TOOLING
)

private val applicationOnlyDependencies = listOf(
    COMPOSE_ACTIVITY
)

val libs: VersionCatalog = getVersionCatalog()

pluginManager.apply(
    libs.getPluginId(PLUGIN_KOTLIN_COMPOSE)
)

plugins.withId(ANDROID_APPLICATION) {
    extensions.configure<ApplicationExtension>(ANDROID) {
        buildFeatures {
            compose = true
        }
    }

    dependencies {
        addDependencies(projectLibs, applicationOnlyDependencies)
    }
}

plugins.withId(ANDROID_LIBRARY) {
    extensions.configure<LibraryExtension>(ANDROID) {
        buildFeatures {
            compose = true
        }
    }
}

dependencies {
    addBOM(libs, COMPOSE_BOM)
    addDependencies(libs, implementationDependencies)
    addDebugDependencies(libs, debugDependencies)
}
