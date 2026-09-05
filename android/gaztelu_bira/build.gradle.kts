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

import extensions.getComponentVersionName
import extensions.getCustomVersionCode
import java.util.Properties

plugins {
    alias(libs.plugins.convention.application)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.convention.firebase)
    alias(libs.plugins.convention.hilt)

    // Non-convention
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val localProperties = Properties().apply {
    rootProject.file("local.properties")
        .takeIf { it.exists() }
        ?.inputStream()
        ?.use { load(it) }
}
val googleClientId: String = localProperties.getProperty("googleClientId").orEmpty()

android {
    namespace = "com.sgale.gaztelubira"

    defaultConfig {
        applicationId = "com.sgale.gaztelubira"
        versionCode = getCustomVersionCode()
        versionName = getComponentVersionName()

        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$googleClientId\"")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime)

    /**
     * Project
     */
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:screens"))

    /**
     * Multiplatform
     */
    implementation(project(":multiplatform:designsystem"))
    implementation(project(":multiplatform:ui"))
}

/**
 * Compose Multiplatform wires its resources into an Android module's assets only for the classic
 * `com.android.library` plugin. `:multiplatform:ui` uses AGP's KMP library plugin, where CMP leaves its
 * `copyAndroidMainComposeResourcesToAndroidAssets` task without an output directory — so the
 * shared module's `composeResources` never reach the APK and every `Res.drawable.*` fails at
 * runtime with MissingResourceException.
 *
 * Until that gap closes, the app packages them itself, under the exact path the resource reader
 * looks in: `assets/composeResources/<packageOfResClass>/`.
 */
abstract class CopySharedComposeResources : DefaultTask() {

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val source: ConfigurableFileCollection

    @get:Input
    abstract val resourcesPackage: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Inject
    abstract val fs: FileSystemOperations

    @TaskAction
    fun copy() {
        fs.sync {
            from(source)
            into(outputDir.get().dir("composeResources/${resourcesPackage.get()}"))
        }
    }
}

val copySharedUiComposeResources by tasks.registering(CopySharedComposeResources::class) {
    dependsOn(":multiplatform:designsystem:prepareComposeResourcesTaskForCommonMain")
    source.from(
        project(":multiplatform:designsystem").layout.buildDirectory
            .dir("generated/compose/resourceGenerator/preparedResources/commonMain/composeResources")
    )
    resourcesPackage.set("com.sgale.gaztelubira.multiplatform.ui.resources")
    outputDir.set(layout.buildDirectory.dir("sharedUiComposeResources"))
}

androidComponents {
    onVariants { variant ->
        variant.sources.assets?.addGeneratedSourceDirectory(
            copySharedUiComposeResources,
            CopySharedComposeResources::outputDir
        )
    }
}
