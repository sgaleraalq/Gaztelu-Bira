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

internal object Constants {
    internal const val LIBS = "libs"

    /**
     * Plugins
     */
    internal const val ANDROID = "android"
    internal const val ANDROID_APPLICATION = "com.android.application"
    internal const val ANDROID_LIBRARY = "com.android.library"
    internal const val PLUGIN_GOOGLE_SERVICES = "google-services"
    internal const val PLUGIN_HILT = "hilt"
    internal const val PLUGIN_KOTLIN_COMPOSE = "kotlin-compose"
    internal const val PLUGIN_KOTLIN_SERIALIZATION = "kotlin-serialization"
    internal const val PLUGIN_KSP = "ksp"

    /**
     * Dependencies
     */
    internal const val ANDROID_CORE = "androidx-core-ktx"
    internal const val HILT = "hilt-android"
    internal const val HILT_COMPILER = "hilt-compiler"
    internal const val KOTLIN_SERIALIZATION = "kotlinx-serialization"
    internal const val NAV3_RUNTIME = "nav3-runtime"
    internal const val NAV3_UI = "nav3-ui"

    /**
     * Firebase
     */
    internal const val FIREBASE_BOM = "firebase-bom"
    internal const val FIREBASE_ANALYTICS = "firebase-analytics"
    internal const val FIREBASE_CRASHLYTICS = "firebase-crashlytics"
    internal const val FIREBASE_CONFIG = "firebase-config"
    internal const val FIREBASE_FIRESTORE = "firebase-firestore"

    /**
     * Compose
     */
    internal const val COMPOSE_BOM = "compose-bom"
    internal const val COMPOSE_ACTIVITY = "compose-activity"
    internal const val COMPOSE_FOUNDATION = "compose-foundation"
    internal const val COMPOSE_LIFECYCLE = "compose-lifecycle-runtime"
    internal const val COMPOSE_MATERIAL3 = "compose-material3"
    internal const val COMPOSE_MATERIAL_ICONS = "compose-material-icons-extended"
    internal const val COMPOSE_UI = "compose-ui"
    internal const val COMPOSE_UI_TOOLING = "compose-ui-tooling"
    internal const val COMPOSE_UI_TOOLING_PREVIEW = "compose-ui-tooling-preview"
}
