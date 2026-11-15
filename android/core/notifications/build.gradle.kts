/*
 * Designed and developed by 2026 sgale (Sergio Galera)
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

plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.hilt)
}

android {
    namespace = "com.sgale.gaztelubira.core.notifications"
}

/**
 * On purpose this module depends on no other module of the project: it only knows Firebase
 * Messaging and the payload the Cloud Function sends (client/functions). Whoever wants pushes
 * depends on it and calls `MatchNotifications`; nothing here knows about sessions or screens.
 */
dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    implementation(libs.androidx.activity)
    implementation(libs.kotlinx.coroutines.play.services)

    testImplementation(libs.junit)
}
