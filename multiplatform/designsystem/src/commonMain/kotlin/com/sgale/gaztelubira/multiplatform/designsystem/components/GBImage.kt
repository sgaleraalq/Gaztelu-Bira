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

package com.sgale.gaztelubira.multiplatform.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sgale.gaztelubira.multiplatform.designsystem.utils.shimmerEffect

/**
 * Images are handed to the design system, never chosen by it: a `String` for something loaded
 * over the network and a `Painter` for anything already resolved. That keeps these components
 * free of both the app's data model and of any particular resource system — an Android caller
 * resolves `R.drawable.x` and a multiplatform one resolves `Res.drawable.x`, and neither shows
 * up in these signatures.
 */

@Composable
fun GBImage(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    image: String?,
    placeholder: Painter? = null,
    contentScale: ContentScale = Crop
) {
    var isLoading by remember { mutableStateOf(false) }
    val customModifier = if (isLoading) modifier.shimmerEffect() else modifier

    Box(
        modifier = customModifier,
        contentAlignment = Center
    ) {
        AsyncImage(
            modifier = imageModifier,
            model = image,
            contentScale = contentScale,
            contentDescription = null,
            onLoading = { isLoading = true },
            onError = { isLoading = false },
            onSuccess = { isLoading = false },
            error = placeholder,
            fallback = placeholder
        )
    }
}

@Composable
fun GBImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    contentScale: ContentScale = Crop
) {
    Image(
        modifier = modifier,
        painter = painter,
        contentScale = contentScale,
        contentDescription = contentDescription
    )
}

@Composable
fun GBImage(
    modifier: Modifier = Modifier,
    image: ByteArray,
    contentDescription: String? = null,
    contentScale: ContentScale = Crop
) {
    AsyncImage(
        modifier = modifier,
        model = image,
        contentScale = contentScale,
        contentDescription = contentDescription
    )
}

@Composable
fun GBLocalImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentScale: ContentScale = Crop
) {
    Image(
        modifier = modifier,
        painter = painter,
        contentScale = contentScale,
        contentDescription = null
    )
}

@Composable
fun GBAsyncImage(
    modifier: Modifier = Modifier,
    image: String?,
    placeholder: Painter? = null,
    contentScale: ContentScale = Crop,
    isLoading: Boolean = false,
    finishLoading: () -> Unit = {}
) {
    AsyncImage(
        modifier = if (isLoading) modifier.shimmerEffect() else modifier,
        model = image,
        contentScale = contentScale,
        contentDescription = null,
        onError = { finishLoading() },
        onSuccess = { finishLoading() },
        error = placeholder,
        fallback = placeholder
    )
}

@Composable
fun GBPlayerImage(
    modifier: Modifier = Modifier,
    image: String?,
    placeholder: Painter? = null,
    borderColor: Color = White
) {
    GBImage(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50)
            ),
        imageModifier = Modifier.fillMaxSize(),
        image = image,
        placeholder = placeholder
    )
}
