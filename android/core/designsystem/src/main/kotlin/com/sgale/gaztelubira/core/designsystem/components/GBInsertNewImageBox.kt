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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.FacePlayer
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.core.domain.utils.CommonImage

@Composable
fun GBImageBoxRequester(
    modifier: Modifier,
    text: String,
    iconSize: Dp = 32.dp,
    commonImage: CommonImage?,
    onClick: () -> Unit = {},
    removeImage: () -> Unit
) {
    Row(
        modifier = modifier
            .background(gray_box_in_black_bg, RoundedCornerShape(8.dp)),
        verticalAlignment = CenterVertically
    ) {
        GBText(
            modifier = Modifier
                .clickable { onClick() }
                .padding(16.dp)
                .weight(1f),
            text = text,
            style = gBTypography().bodyMedium
        )

        GBInsertImage(
            modifier = Modifier.padding(12.dp).size(iconSize),
            imageModifier = Modifier.padding(4.dp),
            iconModifier = Modifier.padding(4.dp),
            image = commonImage,
            iconSize = iconSize,
            onClick = {},
            removeImage = { removeImage() },
            isClickable = true,
            saverStatus = FacePlayer,
            enableExpansion = true
        )
    }
}
