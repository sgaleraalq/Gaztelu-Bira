/*
 * Designed and developed by 2026 sgaleraalq (Sergio Galera)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    /**
     * The Kotlin Gradle plugin and AGP already sit on the build classpath through `build-logic`,
     * so they are applied by id — requesting a version here would clash with that classpath.
     *
     * Since AGP 9.0 `com.android.library` is incompatible with the Kotlin Multiplatform plugin;
     * the Android target of a KMP module is configured through `com.android.kotlin.multiplatform.library`
     * and its `android { }` DSL instead (`androidLibrary { }`, its former name, is deprecated).
     */
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.plugin.compose")

    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
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
            export(project(":multiplatform:designsystem"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            /**
             * Compottie
             */
            implementation(libs.compottie)
            implementation(libs.compottie.lite)
            implementation(libs.compottie.dot)
            implementation(libs.compottie.network)
            implementation(libs.compottie.resources)

            api(project(":multiplatform:designsystem"))
        }
    }
}

/**
 * `Res` lives in `:multiplatform:designsystem`, the only owner of `composeResources`. The Compose
 * plugin would still emit an empty `Res` here, and with no package pinned it falls back to one
 * derived from the Gradle project name — "Gaztelu Bira", with a space, which D8 refuses to dex.
 */
compose.resources {
    generateResClass = never
}
