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

package extensions

import common.Constants.LIBS
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

private const val KEY_COMPILE_SDK = "compileSdk"
private const val KEY_COMPONENT_VERSION_CODE = "componentVersionCode"
private const val KEY_COMPONENT_VERSION_NAME = "componentVersionName"
private const val KEY_JDK_LEVEL = "jdkLevel"
private const val KEY_MIN_SDK = "minSdk"
private const val KEY_TARGET_SDK = "targetSdk"
private const val KEY_VERSION = "VERSION_"

private val VERSION_TAG_PATTERN = Regex("^([0-9]|[1-9][0-9]*)\\.[0-9]{1,2}\\.[0-9]{1,2}$")

internal val Project.projectLibs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named(LIBS)

internal fun Project.getVersionCatalog(): VersionCatalog = projectLibs

/**
 * Build Tools
 */
internal fun Project.getCompileSdkVersion(): Int =
    projectLibs.findVersionAsInt(KEY_COMPILE_SDK)

internal fun Project.getComponentVersionCode(): String =
    (findProperty(KEY_COMPONENT_VERSION_CODE)?.toString()?.toInt()
        ?: getDefaultComponentVersionCode()).toString()

internal fun Project.getJavaVersion(): JavaVersion =
    JavaVersion.valueOf("$KEY_VERSION${getJdkLevel()}")

internal fun Project.getJdkLevel(): Int =
    projectLibs.findVersionAsInt(KEY_JDK_LEVEL)

internal fun Project.getMinSdkVersion(): Int =
    projectLibs.findVersionAsInt(KEY_MIN_SDK)

internal fun Project.getTargetSdkVersion(): Int =
    projectLibs.findVersionAsInt(KEY_TARGET_SDK)

/**
 * Public
 */
fun Project.getCustomVersionCode(): Int {
    val componentVersionCode: String by rootProject.properties

    return componentVersionCode.toInt() * 1000
}

fun Project.getComponentVersionName(): String =
    (findProperty(KEY_COMPONENT_VERSION_NAME)?.toString()
        ?: getDefaultComponentVersionName()).also { componentVersionName ->
        require(VERSION_TAG_PATTERN.matches(componentVersionName)) {
            "The component version must contain three " +
                    "components (mayor, minor, patch level) (now: $componentVersionName)."
        }
    }

private fun Project.getDefaultComponentVersionCode(): Int =
    projectLibs.findVersionAsInt(KEY_COMPONENT_VERSION_CODE)

private fun Project.getDefaultComponentVersionName(): String =
    projectLibs.findVersionAsString(KEY_COMPONENT_VERSION_NAME)