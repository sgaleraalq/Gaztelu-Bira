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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun GBInsertButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean,
    loading: Boolean,
    progressColor: Color = White,
    onInsert: () -> Unit
) {
    val density = LocalDensity.current
    var measuredHeight by remember { mutableStateOf(0.dp) }

    val fallbackHeight = 56.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (measuredHeight > 0.dp) measuredHeight else fallbackHeight),
        contentAlignment = Center
    ) {
        if (!loading) {
            GBElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size ->
                        measuredHeight = with(density) { size.height.toDp() }
                    },
                text = text,
                enabled = enabled,
                onClick = { onInsert() }
            )
        } else {
            GBProgressDialog(Modifier.fillMaxHeight(), true, progressColor)
        }
    }
}
