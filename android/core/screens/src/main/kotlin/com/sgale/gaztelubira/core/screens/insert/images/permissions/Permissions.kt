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

package com.sgale.gaztelubira.core.screens.insert.images.permissions

import androidx.compose.runtime.Immutable

@Immutable
class Permissions internal constructor(
    private val granted: (MediaPermission) -> Boolean,
    private val request: (MediaPermission, (Boolean) -> Unit) -> Unit
) {
    internal fun isGranted(permission: MediaPermission): Boolean = granted(permission)

    internal fun withPermission(
        permission: MediaPermission,
        onDenied: () -> Unit = {},
        onGranted: () -> Unit
    ) {
        if (granted(permission)) {
            onGranted()
            return
        }

        request(permission) { isGranted ->
            if (isGranted) onGranted() else onDenied()
        }
    }
}
