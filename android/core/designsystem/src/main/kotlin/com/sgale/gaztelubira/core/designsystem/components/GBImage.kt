/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.designsystem.components

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sgale.gaztelubira.core.designsystem.R
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.BodyPlayer
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.FacePlayer
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.ManagerPlayer
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.Team
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.Undefined
import com.sgale.gaztelubira.core.designsystem.utils.shimmerEffect
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.model.player.Position.GoalKeeper

enum class SaverStatus {
    FacePlayer, BodyPlayer, ManagerPlayer, Team, Undefined
}

@Composable
fun GBPlayerImage(
    modifier: Modifier = Modifier,
    image: String?,
    borderColor: Color = White,
    saverStatus: SaverStatus = FacePlayer
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
        saverStatus = saverStatus
    )
}

@Composable
fun GBPlayerImage(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int
) {
    GBImage(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = White,
                shape = RoundedCornerShape(50)
            ),
        image = image
    )
}

@Composable
fun GBImage(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    image: String?,
    contentScale: ContentScale = Crop,
    saverStatus: SaverStatus
) {
    val saverImage = when (saverStatus) {
        FacePlayer -> R.drawable.img_face_player
        BodyPlayer -> R.drawable.img_body_player
        Team -> R.drawable.img_no_football_logo
        Undefined -> R.drawable.img_placeholder
        ManagerPlayer -> R.drawable.img_manager
    }

    var isLoading by remember { mutableStateOf(false) }
    val customModifier = if (isLoading) {
        modifier.shimmerEffect()
    } else {
        modifier
    }

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
            error = painterResource(saverImage),
            fallback = painterResource(saverImage)
        )
    }
}

@Composable
fun GBImage(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    scale: ContentScale = Crop
) {
    Image(
        modifier = modifier,
        painter = painterResource(image),
        contentScale = scale,
        contentDescription = null
    )
}

@Composable
fun GBImage(
    modifier: Modifier = Modifier,
    image: ByteArray,
    contentScale: ContentScale = Crop
) {
    AsyncImage(
        modifier = modifier,
        model = image,
        contentScale = contentScale,
        contentDescription = stringResource(R.string.description_insert_player_image)
    )
}

@Composable
fun GBLocalImage(
    modifier: Modifier,
    @DrawableRes image: Int = R.drawable.img_gaztelu_bira,
    scale: ContentScale = Crop
) {
    Image(
        modifier = modifier,
        painter = painterResource(image),
        contentScale = scale,
        contentDescription = null,
    )
}

@Composable
fun GBAsyncImage(
    modifier: Modifier,
    image: String?,
    contentScale: ContentScale,
    isLoading: Boolean,
    finishLoading: () -> Unit,
    @DrawableRes saverImage: Int = R.drawable.img_face_player
) {
    AsyncImage(
        modifier = if (isLoading) modifier.shimmerEffect() else modifier ,
        model = image,
        contentScale = contentScale,
        contentDescription = null,
        onError = { finishLoading() },
        onSuccess = { finishLoading() },
        error = painterResource(saverImage),
        fallback = painterResource(saverImage)
    )
}

@DrawableRes
fun getSaverImage(
    position: Position?
): Int {
    return when (position) {
        GoalKeeper -> R.drawable.img_body_player // todo
        null -> R.drawable.img_body_player
        else -> R.drawable.img_body_player
    }
}
