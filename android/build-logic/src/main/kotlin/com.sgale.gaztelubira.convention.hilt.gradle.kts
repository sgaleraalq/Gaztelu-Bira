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
