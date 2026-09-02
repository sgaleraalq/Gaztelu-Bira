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

package com.sgale.gaztelubira.multiplatform.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.style.bottom_nav_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.bottom_nav_selected

@Composable
fun RowScope.GBBottomNavItem(
    isSelected: Boolean,
    content: @Composable () -> Unit,
    navigate: () -> Unit,
    isMiddleScreen: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = animateColorAsState(
        if (isSelected) bottom_nav_selected else bottom_nav_bg
    )

    Box(
        contentAlignment = Center,
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { navigate() }
            )
            .wrapContentWidth()
            .size(48.dp)
            .clip(if (isMiddleScreen) RoundedCornerShape(50) else MaterialTheme.shapes.medium)
            .background(backgroundColor.value)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = { navigate() }
            )
    ) {
        content()
    }
}
