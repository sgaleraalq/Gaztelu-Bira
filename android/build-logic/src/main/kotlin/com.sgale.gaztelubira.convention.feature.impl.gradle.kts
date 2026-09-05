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

import com.android.build.api.dsl.LibraryExtension
import common.Constants.NAV3_RUNTIME
import common.ProjectConstants.PROJECT_COMMON
import common.ProjectConstants.PROJECT_DOMAIN
import common.ProjectConstants.PROJECT_NAVIGATION
import common.ProjectConstants.PROJECT_PREVIEW
import extensions.addDependencies
import extensions.addProjectDependencies
import extensions.getVersionCatalog

plugins {
    id("com.sgale.gaztelubira.convention.library")
    id("com.sgale.gaztelubira.convention.compose")
    id("com.sgale.gaztelubira.convention.hilt")
}

/**
 * Each feature gets its own BuildConfig so debug-only UI can gate on its own `DEBUG` flag
 * instead of borrowing another module's generated class.
 */
extensions.configure<LibraryExtension> {
    buildFeatures {
        buildConfig = true
    }
}

val featureProjectDependencies = listOf(
    PROJECT_COMMON,
    PROJECT_DOMAIN,
    PROJECT_NAVIGATION,
    PROJECT_PREVIEW
)

val externalDependencies = listOf(
    NAV3_RUNTIME
)

val libs = getVersionCatalog()

dependencies {
    addProjectDependencies(featureProjectDependencies)
    addDependencies(libs, externalDependencies)
}
