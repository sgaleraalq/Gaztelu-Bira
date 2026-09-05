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

package com.sgale.gaztelubira.core.domain.usecase

import com.sgale.gaztelubira.core.domain.utils.PermissionBridge
import com.sgale.gaztelubira.core.domain.utils.PermissionResultCallback
import com.sgale.gaztelubira.core.domain.utils.PermissionType.GALLERY
import javax.inject.Inject

class ShowGallery @Inject constructor(
    private val permissionBridge: PermissionBridge
) {
    operator fun invoke(
        onLaunchGallery: () -> Unit,
        onPermissionsDenied: () -> Unit
    ) {
        requestGalleryPermission(
            initGallery = { onLaunchGallery() },
            onPermissionsDenied = { onPermissionsDenied() }
        )
    }

    private fun requestGalleryPermission(
        initGallery: () -> Unit,
        onPermissionsDenied: () -> Unit
    ) {
        permissionBridge.requestPermission(
            permission = GALLERY,
            callback = object : PermissionResultCallback {
                override fun onPermissionsGranted() {
                    initGallery()
                }

                override fun onPermissionsDenied(isPermanentDenied: Boolean) {
                    onPermissionsDenied()
                }
            }
        )
    }
}
