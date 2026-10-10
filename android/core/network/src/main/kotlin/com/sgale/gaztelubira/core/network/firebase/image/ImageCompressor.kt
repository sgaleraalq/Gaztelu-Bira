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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import androidx.core.graphics.createBitmap
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180
import androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270
import androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90
import androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

/**
 * Converts any image picked by the user into a small [ImageFormat] file, ready to be uploaded.
 */
internal class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * @param uri Uri of the source image (any format Android can decode).
     * @param maxSize Max size in pixels of the longest side of the result.
     * @param format Output format. With [ImageFormat.JPEG] transparent areas are filled with white,
     * since JPEG has no alpha channel; [ImageFormat.PNG] keeps them.
     */
    suspend fun compress(uri: Uri, maxSize: Int, format: ImageFormat): ByteArray = withContext(IO) {
        val source = decodeSampled(uri, maxSize)
        val transformed = source.scaleAndRotate(maxSize, readRotation(uri))
        val output = if (format.supportsAlpha) transformed else transformed.onWhiteBackground()

        val bytes = ByteArrayOutputStream().use { stream ->
            output.compress(format.compressFormat, format.quality, stream)
            stream.toByteArray()
        }

        listOf(source, transformed, output).distinct().forEach(Bitmap::recycle)
        bytes
    }

    /**
     * Decodes the image already downsampled, so big photos never get fully loaded into memory.
     */
    private fun decodeSampled(uri: Uri, maxSize: Int): Bitmap {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        open(uri).use { BitmapFactory.decodeStream(it, null, bounds) }
        require(bounds.outWidth > 0 && bounds.outHeight > 0) { "Unsupported image format" }

        var sampleSize = 1
        while (maxOf(bounds.outWidth, bounds.outHeight) / (sampleSize * 2) >= maxSize) {
            sampleSize *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        return open(uri).use { BitmapFactory.decodeStream(it, null, options) }
            ?: throw IllegalArgumentException("Unsupported image format")
    }

    private fun readRotation(uri: Uri): Float =
        runCatching {
            open(uri).use { stream ->
                when (ExifInterface(stream).getAttributeInt(TAG_ORIENTATION, 0)) {
                    ORIENTATION_ROTATE_90 -> 90f
                    ORIENTATION_ROTATE_180 -> 180f
                    ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
            }
        }.getOrDefault(0f)

    private fun Bitmap.scaleAndRotate(maxSize: Int, rotation: Float): Bitmap {
        val scale = minOf(1f, maxSize.toFloat() / maxOf(width, height))
        if (scale == 1f && rotation == 0f) return this

        val matrix = Matrix().apply {
            postScale(scale, scale)
            postRotate(rotation)
        }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun Bitmap.onWhiteBackground(): Bitmap {
        val result = createBitmap(width, height)
        Canvas(result).apply {
            drawColor(Color.WHITE)
            drawBitmap(this@onWhiteBackground, 0f, 0f, null)
        }
        return result
    }

    private fun open(uri: Uri): InputStream =
        requireNotNull(context.contentResolver.openInputStream(uri)) { "Could not open image" }
}
