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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.style.player_card_name_text_color
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun GBBackButton(
    modifier: Modifier = Modifier,
    color: Color = player_card_name_text_color,
    showBackground: Boolean = false,
    isVisible: Boolean = true,
    onClick: () -> Unit
) {
    var isRunning by remember { mutableStateOf(false) }

    Icon(
        modifier = modifier
            .padding(start = 12.dp)
            .background(
                color = if (showBackground) White else Transparent,
                shape = RoundedCornerShape(50)
            )
            .alpha(if (isVisible) 1f else 0f)
            .clickable {
                if (!isRunning && isVisible) {
                    onClick()
                    isRunning = true
                }
            }
            .padding(12.dp)
            .size(24.dp),
        painter = painterResource(Res.drawable.ic_arrow_back),
        contentDescription = null,
        tint = color
    )
}
