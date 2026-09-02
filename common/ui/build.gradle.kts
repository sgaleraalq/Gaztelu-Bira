plugins {
    /**
     * The Kotlin Gradle plugin and AGP already sit on the build classpath through `build-logic`,
     * so they are applied by id — requesting a version here would clash with that classpath.
     *
     * Since AGP 9.0 `com.android.library` is incompatible with the Kotlin Multiplatform plugin;
     * the Android target of a KMP module is configured through `com.android.kotlin.multiplatform.library`
     * and its `androidLibrary { }` DSL instead.
     */
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.plugin.compose")

    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.sgale.gaztelubira.multiplatform.ui"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "GazteluBiraUI"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.okhttp)
        }
    }
}
