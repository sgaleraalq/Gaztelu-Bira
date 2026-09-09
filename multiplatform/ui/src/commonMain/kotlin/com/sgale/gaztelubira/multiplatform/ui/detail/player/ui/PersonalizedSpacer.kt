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

package com.sgale.gaztelubira.multiplatform.ui.detail.player.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryBlue

@Composable
internal fun PersonalizedSpacer(itemHeight: Int) {
    Column(
        modifier = Modifier
            .width(8.dp)
            .height(with(LocalDensity.current) { itemHeight.toDp() })
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        repeat(8) {
            DiagonalLine()
        }
    }
}

@Composable
private fun DiagonalLine() {
    Canvas(
        modifier = Modifier.fillMaxWidth().height(5.dp)
    ) {
        val start = Offset(0f, 0f)
        val end = Offset(size.width * 1f, size.height * 1f)

        drawLine(
            color = primaryBlue,
            start = start,
            end = end,
            strokeWidth = 4f
        )
    }
}
