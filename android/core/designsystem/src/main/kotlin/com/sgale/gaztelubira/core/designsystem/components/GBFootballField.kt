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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale.Companion.FillWidth
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.FacePlayer
import com.sgale.gaztelubira.core.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.core.designsystem.model.LineUpPosition
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.gbmultiplatform.domain.model.player.PlayerModel
import gbmultiplatform.core.design_system.generated.resources.Res
import gbmultiplatform.core.design_system.generated.resources.img_football_field
import kotlinx.coroutines.delay

/**
 * This component will define the football field layout.
 * @param [formation] the match formation to be displayed on the field.
 */
@Composable
fun GBFootballField(
    modifier: Modifier = Modifier,
    showAnimation: Boolean,
    players: Map<Int, PlayerModel?>,
    formation: LineUpFormation,
    onAnimationFinished: () -> Unit = {},
    onPlayerSelected: (LineUpPosition, Int) -> Unit= { _, _ -> }
) {
    var boxWidthPx by remember { mutableFloatStateOf(0f) }
    var boxHeightPx by remember { mutableFloatStateOf(0f) }
    val visiblePlayers = remember { mutableStateListOf<Int>() }

    LaunchedEffect(showAnimation) {
        if (showAnimation) {
            formation.positions.forEachIndexed { index, _ ->
                visiblePlayers.add(index)
                delay(100L)
            }
            onAnimationFinished()
        } else {
            visiblePlayers.addAll(formation.positions.indices)
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                boxWidthPx = layoutCoordinates.size.width.toFloat()
                boxHeightPx = layoutCoordinates.size.height.toFloat()
            }
    ) {
        GBLocalImage(
            modifier = Modifier.fillMaxSize(),
            image = Res.drawable.img_football_field,
            scale = FillWidth
        )

        formation.positions.forEachIndexed { index, position ->
            if (boxWidthPx > 0 && boxHeightPx > 0) {
                GBPlayerPosition(
                    player = players[index],
                    percentX = position.x,
                    percentY = position.y,
                    fieldWidthPx = boxWidthPx,
                    fieldHeightPx = boxHeightPx,
                    visible = visiblePlayers.contains(index),
                    onPlayerSelected = {
                        onPlayerSelected(position.position, index)
                    }
                )
            }
        }
    }
}

@Composable
fun GBPlayerPosition(
    player: PlayerModel?,
    percentX: Float,
    percentY: Float,
    fieldWidthPx: Float,
    fieldHeightPx: Float,
    visible: Boolean,
    onPlayerSelected: () -> Unit
) {
    var columnWidth by remember { mutableIntStateOf(0) }
    var columnHeight by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .onGloballyPositioned {
                columnWidth = it.size.width
                columnHeight = it.size.height
            }
            .wrapContentSize()
            .graphicsLayer {
                val targetX = fieldWidthPx * percentX
                val targetY = fieldHeightPx * percentY

                translationX = targetX - columnWidth / 2f
                translationY = targetY - columnHeight / 2f
            }
    ) {
        AnimatedVisibility(
            modifier = Modifier.clickable { onPlayerSelected() },
            visible = visible,
            enter = fadeIn(tween(150)) + scaleIn(initialScale = 0.5f)
        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(4.dp)
            ) {
                GBImage(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(50)),
                    image = player?.faceImage,
                    saverStatus = FacePlayer
                )
                GBText(
                    text = player?.name,
                    style = gBTypography().bodySmall.copy(
                        fontWeight = Bold
                    )
                )
            }
        }
    }
}
