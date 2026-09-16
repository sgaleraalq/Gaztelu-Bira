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

package com.sgale.gaztelubira.multiplatform.ui.insert.player.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography

@Composable
internal fun DorsalDialog(
    dorsals: List<Int>,
    onDorsalClicked: (Int) -> Unit,
    dismiss: () -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = Fixed(5),
        horizontalArrangement = spacedBy(12.dp),
        verticalArrangement = spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(dorsals) { dorsal ->
            DorsalCard(
                dorsal = dorsal,
                onDorsalClicked = onDorsalClicked,
                dismiss = dismiss
            )
        }
    }
}

@Composable
private fun DorsalCard(
    dorsal: Int,
    onDorsalClicked: (Int) -> Unit,
    dismiss: () -> Unit
) {
    Box(
        modifier = Modifier.size(40.dp)
            .clip(CircleShape)
            .background(White)
            .clickable {
                onDorsalClicked(dorsal)
                dismiss()
            },
        contentAlignment = Center
    ) {
        GBText(
            text = dorsal.toString(),
            style = gBTypography().bodyMedium,
            textColor = Black,
            alignment = TextAlign.Center
        )
    }
}
