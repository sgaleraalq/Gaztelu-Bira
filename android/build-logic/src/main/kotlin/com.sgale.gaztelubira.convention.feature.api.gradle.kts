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
