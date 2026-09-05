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

import android.content.ContentResolver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.sgale.gaztelubira.core.domain.utils.CommonImage.FromGallery

// GalleryManager.android.kt
@Composable
fun rememberGalleryManager(
    onResult: (CommonImage?) -> Unit
): GalleryManager {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver

    val galleryLauncher =
        rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            uri?.let {
                val uri = it.toString()
                onResult(
                    FromGallery(
                        uri = uri,
                        mimeType = contentResolver.getType(it),
                    )
                )
            }
        }

    return remember {
        GalleryManager(onLaunch = {
            galleryLauncher.launch(
                PickVisualMediaRequest(
                    mediaType = ImageOnly
                )
            )
        })
    }
}

class GalleryManager(
    private val onLaunch: () -> Unit
) {
    fun launch() {
        onLaunch()
    }
}
