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

package com.sgale.gaztelubira.core.domain.utils

enum class PermissionType { CAMERA, GALLERY }

interface PermissionsBridgeListener {
    fun requestPermission(permission: PermissionType, callback: PermissionResultCallback)
    fun isPermissionsGranted(permission: PermissionType): Boolean
}

interface PermissionResultCallback {
    fun onPermissionsGranted()
    fun onPermissionsDenied(isPermanentDenied: Boolean)
}

class PermissionBridge {
    private var listener: PermissionsBridgeListener? = null

    fun setListener(listener: PermissionsBridgeListener) {
        this.listener = listener
    }

    fun requestPermission(permission: PermissionType, callback: PermissionResultCallback) {
        listener?.requestPermission(permission, callback) ?: error("Callback handler not set")
    }

    fun isPermissionGranted(permission: PermissionType): Boolean {
        return listener?.isPermissionsGranted(permission) ?: false
    }
}
