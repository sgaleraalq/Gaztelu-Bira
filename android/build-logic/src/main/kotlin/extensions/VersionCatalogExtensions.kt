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

import org.gradle.api.artifacts.VersionCatalog

internal fun VersionCatalog.findVersionAsBoolean(alias: String): Boolean =
    findVersionAsString(alias).toBoolean()

internal fun VersionCatalog.findVersionAsByte(alias: String): Byte =
    findVersionAsString(alias).toByte()

internal fun VersionCatalog.findVersionAsDouble(alias: String): Double =
    findVersionAsString(alias).toDouble()

internal fun VersionCatalog.findVersionAsFloat(alias: String): Float =
    findVersionAsString(alias).toFloat()

internal fun VersionCatalog.findVersionAsInt(alias: String): Int =
    findVersionAsString(alias).toInt()

internal fun VersionCatalog.findVersionAsLong(alias: String): Long =
    findVersionAsString(alias).toLong()

internal fun VersionCatalog.findVersionAsShort(alias: String): Short =
    findVersionAsString(alias).toShort()

internal fun VersionCatalog.findVersionAsString(alias: String): String =
    findVersion(alias).get().requiredVersion

internal fun VersionCatalog.getLibrary(lib: String) =
    findLibrary(lib).get()

internal fun VersionCatalog.getPluginId(plugin: String): String =
    findPlugin(plugin).get().get().pluginId
