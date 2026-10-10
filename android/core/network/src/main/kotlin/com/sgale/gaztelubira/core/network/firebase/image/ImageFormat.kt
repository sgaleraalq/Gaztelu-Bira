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

package com.sgale.gaztelubira.core.network.firebase.image

import android.graphics.Bitmap.CompressFormat

/**
 * Formats the app uploads to Storage.
 *
 * @param quality Ignored by PNG, which is lossless.
 */
internal enum class ImageFormat(
    val extension: String,
    val mimeType: String,
    val compressFormat: CompressFormat,
    val quality: Int,
    val supportsAlpha: Boolean
) {
    JPEG(
        extension = "jpg",
        mimeType = "image/jpeg",
        compressFormat = CompressFormat.JPEG,
        quality = 85,
        supportsAlpha = false
    ),
    PNG(
        extension = "png",
        mimeType = "image/png",
        compressFormat = CompressFormat.PNG,
        quality = 100,
        supportsAlpha = true
    )
}
