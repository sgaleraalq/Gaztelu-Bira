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
import org.gradle.kotlin.dsl.DependencyHandlerScope

private const val ANDROID_TEST_IMPLEMENTATION = "androidTestImplementation"
private const val DEBUG_IMPLEMENTATION = "debugImplementation"
private const val IMPLEMENTATION = "implementation"
private const val KSP = "ksp"
private const val TEST_IMPLEMENTATION = "testImplementation"

/**
 * Version Catalog related
 */

internal fun DependencyHandlerScope.addBOM(
    libs: VersionCatalog,
    dependency: String
) {
    add(IMPLEMENTATION, platform(libs.getLibrary(dependency)))
}

internal fun DependencyHandlerScope.addAndroidTestDependency(
    libs: VersionCatalog,
    dependency: String
) {
    add(ANDROID_TEST_IMPLEMENTATION, libs.getLibrary(dependency))
}

internal fun DependencyHandlerScope.addAndroidTestDependencies(
    libs: VersionCatalog,
    dependencies: List<String>
) {
    dependencies.forEach { dependency ->
        addAndroidTestDependency(libs, dependency)
    }
}

internal fun DependencyHandlerScope.addDebugDependency(
    libs: VersionCatalog,
    dependency: String
) {
    add(DEBUG_IMPLEMENTATION, libs.getLibrary(dependency))
}

internal fun DependencyHandlerScope.addDebugDependencies(
    libs: VersionCatalog,
    dependencies: List<String>
) {
    dependencies.forEach { dependency ->
        addDebugDependency(libs, dependency)
    }
}

internal fun DependencyHandlerScope.addDependency(
    libs: VersionCatalog,
    dependency: String
) {
    add(IMPLEMENTATION, libs.getLibrary(dependency))
}

internal fun DependencyHandlerScope.addDependencies(
    libs: VersionCatalog,
    dependencies: List<String>
) {
    dependencies.forEach { dependency ->
        addDependency(libs, dependency)
    }
}

internal fun DependencyHandlerScope.addKspDependency(
    libs: VersionCatalog,
    kspDependency: String
) {
    add(KSP, libs.getLibrary(kspDependency))
}

internal fun DependencyHandlerScope.addProjectDependency(
    projectDependency: String
) {
    add(IMPLEMENTATION, project(projectDependency))
}

internal fun DependencyHandlerScope.addProjectDependencies(
    projectDependencies: List<String>
) {
    projectDependencies.forEach { dependency ->
        addProjectDependency(dependency)
    }
}

internal fun DependencyHandlerScope.addTestDependency(
    libs: VersionCatalog,
    dependency: String
) {
    add(TEST_IMPLEMENTATION, libs.getLibrary(dependency))
}

internal fun DependencyHandlerScope.addTestDependencies(
    libs: VersionCatalog,
    dependencies: List<String>
) {
    dependencies.forEach { dependency ->
        addTestDependency(libs, dependency)
    }
}