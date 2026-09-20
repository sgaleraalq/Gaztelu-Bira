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
import common.Constants.FIREBASE_AUTH
import common.Constants.FIREBASE_BOM
import common.Constants.FIREBASE_CONFIG
import common.Constants.FIREBASE_CRASHLYTICS
import common.Constants.FIREBASE_FIRESTORE
import common.Constants.FIREBASE_STORAGE
import extensions.addBOM
import extensions.addDependencies
import extensions.getVersionCatalog

val libs = getVersionCatalog()

val firebaseDependencies = listOf(
    FIREBASE_ANALYTICS,
    FIREBASE_AUTH,
    FIREBASE_CRASHLYTICS,
    FIREBASE_CONFIG,
    FIREBASE_FIRESTORE,
    FIREBASE_STORAGE
)

dependencies {
    addBOM(libs, FIREBASE_BOM)
    addDependencies(libs, firebaseDependencies)
}
