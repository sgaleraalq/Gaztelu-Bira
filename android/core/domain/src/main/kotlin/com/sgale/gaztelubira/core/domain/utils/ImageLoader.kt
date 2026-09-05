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

interface ImageLoader {
    suspend fun loadImage(
        uri: String,
        isFrontCamera: Boolean,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int
    ): ByteArray?
}

class SharedImagesBridge {
    private var imageLoader: ImageLoader? = null

    fun setListener(imageLoader: ImageLoader) {
        this.imageLoader = imageLoader
    }

    suspend fun loadImage(
        uri: String,
        isFrontCamera: Boolean,
        maxWidth: Int = 1080,
        maxHeight: Int = 1080,
        quality: Int = 90
    ): ByteArray? {
        return imageLoader?.loadImage(
            uri,
            isFrontCamera,
            maxWidth,
            maxHeight,
            quality
        ) ?: error("ImageLoader not set")
    }
}
