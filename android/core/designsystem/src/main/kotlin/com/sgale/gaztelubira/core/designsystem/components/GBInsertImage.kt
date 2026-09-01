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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sgale.gaztelubira.core.designsystem.style.gray_box_in_black_bg
import com.gbmultiplatform.domain.utils.CommonImage
import com.sgale.gaztelubira.core.designsystem.R
import gbmultiplatform.core.design_system.generated.resources.Res
import gbmultiplatform.core.design_system.generated.resources.ic_camera
import gbmultiplatform.core.design_system.generated.resources.ic_close
import gbmultiplatform.core.design_system.generated.resources.ic_garbage
import org.jetbrains.compose.resources.painterResource

@Composable
fun GBInsertImage(
    modifier: Modifier,
    iconModifier: Modifier,
    imageModifier: Modifier,
    image: CommonImage?,
    iconSize: Dp,
    onClick: () -> Unit,
    removeImage: () -> Unit,
    isClickable: Boolean,
    saverStatus: SaverStatus,
    enableExpansion: Boolean = true
) {
    var expandPicture by remember { mutableStateOf(false) }

    val boxClickableModifier = if (isClickable) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }

    val clickableModifier = if (isClickable) {
        imageModifier.clickable {
            if (enableExpansion) {
                expandPicture = true
            } else {
                removeImage()
            }
        }
    } else {
        imageModifier
    }

    Box(
        modifier = boxClickableModifier.fillMaxWidth(),
        contentAlignment = Center
    ) {
        if (image != null) {
            GBImage(
                modifier = clickableModifier.clip(
                    RoundedCornerShape(12.dp)
                ),
                imageModifier = Modifier.fillMaxSize(),
                image = image.uri,
                contentScale = Crop,
                saverStatus = saverStatus
            )
        } else {
            GBInsertImageIcon(
                modifier = iconModifier,
                iconSize = iconSize
            )
        }
    }

    if (expandPicture) {
        GBExpandedImage(
            uri = image?.uri ?: "",
            saverStatus = saverStatus,
            removeImage = { removeImage() },
            dismiss = { expandPicture = false }
        )
    }
}

@Composable
fun GBInsertImageIcon(
    modifier: Modifier,
    iconSize: Dp
) {
    Box(
        modifier = modifier,
        contentAlignment = Center
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painterResource(R.drawable.ic_camera),
            contentDescription = null,
            tint = Unspecified
        )
    }
}

@Composable
fun GBExpandedImage(
    uri: String,
    saverStatus: SaverStatus,
    removeImage: () -> Unit,
    dismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { dismiss() }
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 36.dp, horizontal = 12.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            ExpandedImageButtons(
                modifier = Modifier.align(End),
                removeImage = removeImage,
                dismiss = dismiss
            )

            GBImage(
                image = uri,
                contentScale = Fit,
                saverStatus = saverStatus
            )
        }
    }
}

@Composable
fun ExpandedImageButtons(
    modifier: Modifier,
    removeImage: () -> Unit,
    dismiss: () -> Unit
) {
    Row(modifier.background(gray_box_in_black_bg)) {
        GBIcon(
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .clickable { dismiss(); removeImage() },
            icon = R.drawable.ic_garbage,
            size = 32.dp
        )

        GBIcon(
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .clickable { dismiss() },
            icon = R.drawable.ic_close,
            size = 32.dp
        )
    }
}
