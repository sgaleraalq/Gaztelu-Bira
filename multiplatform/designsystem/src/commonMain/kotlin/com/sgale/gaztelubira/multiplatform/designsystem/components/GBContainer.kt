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

package com.sgale.gaztelubira.multiplatform.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DEFAULT_SHAPE = 24.dp
val DEFAULT_BG_COLOR = Color(0xFF1D2738)
val DEFAULT_BORDER_COLOR = Color(0xFF94A4B8)

@Composable
fun GBContainer(
    shape: RoundedCornerShape = RoundedCornerShape(DEFAULT_SHAPE),
    bgColor: Color = DEFAULT_BG_COLOR,
    borderColor: Color = DEFAULT_BORDER_COLOR,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .background(bgColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .padding(8.dp),
        contentAlignment = Center
    ) {
        content()
    }
}
