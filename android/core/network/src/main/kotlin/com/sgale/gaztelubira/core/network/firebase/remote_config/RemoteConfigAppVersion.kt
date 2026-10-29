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

package com.sgale.gaztelubira.core.network.firebase.remote_config

import android.content.Context
import com.sgale.gaztelubira.core.domain.repository.AppVersionRemote
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class RemoteConfigAppVersion @Inject constructor(
    private val remoteConfigManager: RemoteConfigManager,
    @param:ApplicationContext private val context: Context
) : AppVersionRemote {
    override suspend fun isSupported(): Boolean {
        remoteConfigManager.fetchAndActivate() ?: return true

        return isVersionAtLeast(getLocalVersion(), remoteConfigManager.getMinAppVersion())
    }

    private fun getLocalVersion(): String =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
}

/**
 * Compares "major.minor.patch" versions part by part from the left, and the first part that differs
 * decides: 1.5.0 is above 1.4.9 even though its patch is lower. A missing part counts as 0, and a
 * suffix after the number ("0-debug") is ignored.
 */
internal fun isVersionAtLeast(current: String, minimum: String): Boolean {
    val currentParts = current.versionParts()
    val minimumParts = minimum.versionParts()

    for (index in 0 until maxOf(currentParts.size, minimumParts.size)) {
        val part = currentParts.getOrElse(index) { 0 }
        val minimumPart = minimumParts.getOrElse(index) { 0 }
        if (part != minimumPart) return part > minimumPart
    }
    return true
}

private fun String.versionParts(): List<Int> =
    split(".").map { part -> part.takeWhile(Char::isDigit).toIntOrNull() ?: 0 }
