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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.lightGray
import com.sgale.gaztelubira.multiplatform.designsystem.style.overlayColor
import com.sgale.gaztelubira.multiplatform.designsystem.style.player_card_background_bottom_gradient
import com.sgale.gaztelubira.multiplatform.designsystem.style.player_card_background_top_gradient
import com.sgale.gaztelubira.multiplatform.designsystem.style.player_card_name_text_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.player_card_stat_text_color
import com.sgale.gaztelubira.multiplatform.designsystem.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_delete
import kotlin.Float.Companion.POSITIVE_INFINITY
import org.jetbrains.compose.resources.painterResource

@Composable
fun GBPlayerCard(
    image: String?,
    name: String,
    stat: String,
    placeholder: Painter? = null,
    onClick: () -> Unit = { }
) {
    val cardBackgroundColor = Brush.linearGradient(
        colors = listOf(
            player_card_background_top_gradient,
            player_card_background_bottom_gradient
        ),
        start = Offset(0f, POSITIVE_INFINITY),
        end = Offset(0f, 0f)
    )

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            modifier = Modifier
                .background(brush = cardBackgroundColor)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = CenterVertically,
        ) {
            GBImage(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(50))
                    .border(width = 1.dp, color = White, shape = RoundedCornerShape(50)),
                image = image,
                placeholder = placeholder
            )
            GBText(
                modifier = Modifier.weight(1f),
                text = name,
                textColor = player_card_name_text_color,
                alignment = Start,
                style = MaterialTheme.typography.bodyMedium
            )
            GBText(
                text = stat,
                textColor = player_card_stat_text_color,
                alignment = Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun GBPlayerCard(
    modifier: Modifier,
    player: GBPlayer,
    placeholder: Painter? = null,
    onPlayerClicked: () -> Unit = {},
    onPlayerRemoved: () -> Unit = {},
    dorsal: String? = null,
    isClickable: Boolean = true,
    showDeletion: Boolean = false,
    showDorsal: Boolean = true,
    dorsalSize: Dp = 24.dp,
    dorsalInternalPadding: Dp = 4.dp,
    dorsalTextSize: TextUnit = 12.sp,
    allowLongTap: Boolean = false
) {
    var longPressed by remember { mutableStateOf(false) }
    val customModifier = if (isClickable) {
        modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { onPlayerClicked() },
                onLongPress = { longPressed = !longPressed },
                onDoubleTap = { longPressed = !longPressed }
            )
        }
    } else  {
        modifier
    }

    Card(
        modifier = customModifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Transparent
        )
    ) {
        Box {
            /**
             * Image
             */
            GBImage(
                modifier = Modifier.fillMaxSize(),
                imageModifier = Modifier.fillMaxSize(),
                image = player.image,
                placeholder = placeholder
            )

            /**
             * Name
             */
            if (allowLongTap) {
                this@Card.AnimatedVisibility(
                    visible = longPressed,
                    modifier = Modifier
                        .align(BottomCenter)
                        .fillMaxWidth()
                ) {
                    GBText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(overlayColor)
                            .padding(vertical = 2.dp),
                        text = player.name,
                        textColor = lightGray,
                        alignment = Center,
                        style = gBTypography().bodySmall
                    )
                }
            }

            /**
             * Dorsal
             */
            if (showDorsal) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(TopStart)
                        .size(dorsalSize)
                        .background(
                            color = White,
                            shape = RoundedCornerShape(50)
                        )
                        .border(
                            width = 1.dp,
                            color = Black,
                            shape = CircleShape
                        )
                        .padding(dorsalInternalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    GBText(
                        text = dorsal ?: player.dorsal.toString(),
                        textColor = gray_box_in_black_bg,
                        style = gBTypography().bodySmall.copy(
                            fontSize = dorsalTextSize
                        )
                    )
                }
            }

            /**
             * Remove icon
             */
            if (showDeletion) {
                GBIcon(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(TopEnd)
                        .size(18.dp)
                        .clickable { onPlayerRemoved() },
                    icon = painterResource(Res.drawable.ic_delete)
                )
            }
        }
    }
}
