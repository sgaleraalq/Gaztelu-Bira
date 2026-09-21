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

package com.sgale.gaztelubira.core.screens.insert.manager.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
internal fun rememberPermissionsManager(): Permissions {
    val context = LocalContext.current

    var pendingResult by remember { mutableStateOf<((Boolean) -> Unit)?>(null) }
    val contract = remember { RequestMultiplePermissions() }

    val launcher = rememberLauncherForActivityResult(contract) { results ->
        pendingResult?.invoke(results.values.any { it })
        pendingResult = null
    }

    return remember(context) {
        Permissions(
            granted = { permission -> permission.isGrantedIn(context) },
            request = { permission, onResult ->
                pendingResult = onResult
                launcher.launch(permission.manifestPermissions().toTypedArray())
            }
        )
    }
}
