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

package common

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import common.Constants.ANDROID_CORE
import extensions.addDependency
import extensions.getCompileSdkVersion
import extensions.getComponentVersionCode
import extensions.getComponentVersionName
import extensions.getJavaVersion
import extensions.getMinSdkVersion
import extensions.getTargetSdkVersion
import extensions.projectLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

const val TYPE_INT = "int"
const val TYPE_STRING = "String"
const val VERSION_CODE = "VERSION_CODE"
const val VERSION_NAME = "VERSION_NAME"

internal fun Project.applyCommonConfiguration(
    extension: CommonExtension
) {
    val jdkLevel = getJavaVersion()
    val minimumSdk = getMinSdkVersion()
    val sdkTarget = getTargetSdkVersion()

    extension.apply {
        compileSdk = getCompileSdkVersion()

        when (this) {
            is ApplicationExtension -> {
                applyConfiguration(
                    minimumSdk = minimumSdk,
                    jdkLevel = jdkLevel,
                    sdkTarget = sdkTarget,
                    versionName = getComponentVersionName(),
                    versionCode = getComponentVersionCode()
                )
            }

            is LibraryExtension -> {
                applyConfiguration(
                    minimumSdk = minimumSdk,
                    sdkTarget = sdkTarget,
                    jdkLevel = jdkLevel
                )
            }
        }

        dependencies {
            addDependency(projectLibs, ANDROID_CORE)
        }
    }
}

private fun ApplicationExtension.applyConfiguration(
    minimumSdk: Int,
    jdkLevel: JavaVersion,
    sdkTarget: Int,
    versionName: String,
    versionCode: String
) {
    defaultConfig {
        minSdk = minimumSdk
        targetSdk = sdkTarget

        buildConfigField(
            TYPE_STRING,
            VERSION_NAME,
            "\"$versionName\""
        )

        buildConfigField(
            TYPE_INT,
            VERSION_CODE,
            versionCode
        )
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = jdkLevel
        targetCompatibility = jdkLevel
    }

    lint {
        targetSdk = sdkTarget
        abortOnError = false
    }
}

private fun LibraryExtension.applyConfiguration(
    minimumSdk: Int,
    jdkLevel: JavaVersion,
    sdkTarget: Int
) {
    defaultConfig {
        minSdk = minimumSdk
    }

    compileOptions {
        sourceCompatibility = jdkLevel
        targetCompatibility = jdkLevel
    }

    lint {
        targetSdk = sdkTarget
        abortOnError = false
    }
}
