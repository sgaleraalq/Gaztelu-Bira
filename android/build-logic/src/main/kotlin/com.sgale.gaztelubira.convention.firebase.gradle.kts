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

import common.Constants.FIREBASE_ANALYTICS
import common.Constants.FIREBASE_BOM
import common.Constants.FIREBASE_CONFIG
import common.Constants.FIREBASE_CRASHLYTICS
import common.Constants.FIREBASE_FIRESTORE
import extensions.addBOM
import extensions.addDependencies
import extensions.getVersionCatalog

/**
 * Wires Firebase into a module via the Firebase BoM, so every Firebase artifact resolves to a
 * mutually compatible version.
 *
 * Only the SDKs that work with no code of their own live here — Crashlytics and Analytics start
 * reporting from the moment they are on the classpath. Anything that needs to be called to be
 * useful (Vertex AI, Firestore, Remote Config) is declared by the module that actually calls it,
 * so an unused SDK never ends up in the APK by accident.
 *
 * The Gradle plugins live on the app module, not here: `com.google.gms.google-services` reads
 * `google-services.json`, and `com.google.firebase.crashlytics` injects the build ID and uploads
 * the mapping file. Both are no-ops when applied to a library module.
 */

val libs = getVersionCatalog()

val firebaseDependencies = listOf(
    FIREBASE_ANALYTICS,
    FIREBASE_CRASHLYTICS,
    FIREBASE_CONFIG,
    FIREBASE_FIRESTORE
)

dependencies {
    addBOM(libs, FIREBASE_BOM)
    addDependencies(libs, firebaseDependencies)
}
