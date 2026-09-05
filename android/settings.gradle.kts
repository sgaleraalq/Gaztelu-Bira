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

import org.gradle.api.initialization.resolve.RepositoriesMode.FAIL_ON_PROJECT_REPOS

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Gaztelu Bira"

include(":gaztelu_bira")

/**
 * Android Only
 */
include(":core:common")
include(":core:data")
include(":core:domain")
include(":core:navigation")
include(":core:preview")
include(":core:screens")

/**
 * Multiplatform
 */
include(":multiplatform:ui")
include(":multiplatform:designsystem")
project(":multiplatform").projectDir = File(settingsDir, "../multiplatform")
project(":multiplatform:ui").projectDir = File(settingsDir, "../multiplatform/ui")
project(":multiplatform:designsystem").projectDir = File(settingsDir, "../multiplatform/designsystem")
