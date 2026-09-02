import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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

        /**
         * Without this the Android compilation emits bytecode for whichever JDK runs Gradle,
         * and D8 fails to dex it against the rest of the project ("Error while dexing").
         */
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(libs.versions.jdkLevel.get()))
                }
            }
        }
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

            /**
             * Images from network. Coil's okhttp fetcher is JVM only, so the multiplatform
             * build goes through the Ktor one and each platform contributes its own engine.
             */
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

/**
 * The generated `Res` class takes its package from the Gradle project name, and this one is
 * "Gaztelu Bira" — with a space. Kotlin swallows it behind backticks, but D8 refuses to dex a
 * class name containing a space, so the package is pinned here instead.
 */
compose.resources {
    packageOfResClass = "com.sgale.gaztelubira.multiplatform.ui.resources"
}
