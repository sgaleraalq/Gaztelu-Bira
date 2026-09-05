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

package com.sgale.gaztelubira.core.data.network.firestore

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
class FbRemoteConfigManager @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {
    companion object {
        const val MINIMUM_FETCH_INTERVAL_SECONDS = 3600L
        private const val MIN_VERSION_RC = "min_version"
    }

    suspend fun fetchAndActivate(): Boolean? {
        return try {
            remoteConfig.fetch(0).await()
            remoteConfig.activate().await()
        } catch (e: Exception) {
            Log.w(FbRemoteConfigManager::class.simpleName, "Fetch failed or throttled", e)
            null
        }
    }

    fun getMinAppVersion(): List<Int> {
        val minVersion = remoteConfig.getString(MIN_VERSION_RC)
        if (minVersion.isBlank()) return listOf(0, 0, 0)
        return minVersion.split(".").mapNotNull { it.toIntOrNull() }
    }
}
