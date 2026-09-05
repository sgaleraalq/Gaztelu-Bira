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

package com.sgale.gaztelubira.core.data

import android.content.Context
import com.sgale.gaztelubira.core.data.network.firestore.FbRemoteConfigManager
import com.sgale.gaztelubira.core.domain.repository.IAppRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val remoteConfigManager: FbRemoteConfigManager,
    @param:ApplicationContext private val context: Context
) : IAppRepository {
    override suspend fun updateAvailable(): Boolean {
        remoteConfigManager.fetchAndActivate() ?: return true

        return getLocalVersion().zip(remoteConfigManager.getMinAppVersion())
            .all { (appVersion, minVersion) -> appVersion >= minVersion }
    }

    private fun getLocalVersion(): List<Int> {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val localVersion =
            packageInfo.versionName?.split(".")?.map { it.toInt() } ?: listOf(0, 0, 0)
        return localVersion
    }
}
