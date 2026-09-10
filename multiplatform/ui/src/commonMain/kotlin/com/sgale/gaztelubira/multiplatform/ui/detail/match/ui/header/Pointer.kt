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

package com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.header

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Thin
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.player_card_name_text_color
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Details
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Lineup
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Loading
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Stats

private const val POINTER_OFFSET = "pointer_offset"
private const val POINTER_MOVE_DURATION = 400

@Composable
internal fun MatchDetailBox(
    modifier: Modifier,
    text: String,
    isHighlighted: Boolean,
    onClick: () -> Unit
) {
    val textStyle = gBTypography().bodyMedium.copy(
        fontWeight = when (isHighlighted) {
            true -> Bold
            false -> Thin
        }
    )

    Box(
        modifier = modifier.clickable { onClick() },
        contentAlignment = Center
    ) {
        GBText(
            text = text,
            textColor = if (isHighlighted) White else player_card_name_text_color,
            style = textStyle
        )
    }
}

@Composable
internal fun MatchDetailPointer(
    state: MatchDetailState
) {
    val density = LocalDensity.current
    var totalWidth by remember { mutableStateOf(0.dp) }

    val targetIndex = when (state) {
        is Details -> 0
        is Lineup -> 1
        is Stats -> 2
        Loading -> return
    }

    val cellWidth = if (totalWidth > 0.dp) totalWidth / 3 else 0.dp
    val pointerPadding = 32.dp
    val offsetX by animateDpAsState(
        targetValue = (cellWidth * targetIndex) + pointerPadding / 2,
        animationSpec = tween(POINTER_MOVE_DURATION, easing = FastOutSlowInEasing),
        label = POINTER_OFFSET
    )

    Box(
        Modifier
            .fillMaxWidth()
            .onGloballyPositioned { totalWidth = with(density) { it.size.width.toDp() } }
            .height(2.dp)
    ) {
        Box(
            Modifier
                .offset(x = offsetX)
                .width(cellWidth - pointerPadding)
                .fillMaxHeight()
                .background(White)
        )
    }
}
