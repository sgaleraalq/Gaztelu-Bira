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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.style.gray_box_in_black_bg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GBDialog(
    modifier: Modifier = Modifier,
    show: Boolean = true,
    color: Color = gray_box_in_black_bg,
    dismiss: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = show
    ) {
        BasicAlertDialog(
            onDismissRequest = dismiss
        ) {
            content(
                Modifier.background(
                    color = color,
                    shape = RoundedCornerShape(12.dp)
                )
            )
        }
    }
}
