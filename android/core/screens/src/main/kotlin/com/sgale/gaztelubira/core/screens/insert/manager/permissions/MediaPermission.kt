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

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import androidx.core.content.ContextCompat.checkSelfPermission

internal enum class MediaPermission {
    CAMERA,
    GALLERY;

    internal fun manifestPermissions(): List<String> = when (this) {
        CAMERA -> listOf(Manifest.permission.CAMERA)
        GALLERY -> when {
            SDK_INT >= UPSIDE_DOWN_CAKE -> listOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
            SDK_INT >= TIRAMISU -> listOf(READ_MEDIA_IMAGES)
            else -> listOf(READ_EXTERNAL_STORAGE)
        }
    }

    internal fun isGrantedIn(context: Context): Boolean =
        manifestPermissions().any { checkSelfPermission(context, it) == PERMISSION_GRANTED }
}
