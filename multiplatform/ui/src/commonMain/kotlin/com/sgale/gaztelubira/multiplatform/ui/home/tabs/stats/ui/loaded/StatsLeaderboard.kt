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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.loaded

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBAsyncImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.leaderboardFirst
import com.sgale.gaztelubira.multiplatform.designsystem.style.leaderboardSecond
import com.sgale.gaztelubira.multiplatform.designsystem.style.leaderboardThird
import com.sgale.gaztelubira.multiplatform.designsystem.style.softGreen
import com.sgale.gaztelubira.multiplatform.designsystem.style.softRed
import com.sgale.gaztelubira.multiplatform.designsystem.utils.shimmerEffect
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStat
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_down
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_up
import org.jetbrains.compose.resources.painterResource
import kotlin.math.absoluteValue

private val LEADER_POSITION_SIZE = 24.dp
private const val FIRST_POSITION_SIZE = 100
private const val SECOND_POSITION_SIZE = 75
private const val THIRD_POSITION_SIZE = 65

private enum class LeaderboardPosition(
    val number: Int,
    val circleSize: Dp,
    val circleColor: Color
) {
    FIRST(
        number = 1,
        circleSize = FIRST_POSITION_SIZE.dp,
        circleColor = leaderboardFirst
    ),
    SECOND(
        number = 2,
        circleSize = SECOND_POSITION_SIZE.dp,
        circleColor = leaderboardSecond
    ),
    THIRD(
        number = 3,
        circleSize = THIRD_POSITION_SIZE.dp,
        circleColor = leaderboardThird
    )
}

@Composable
internal fun StatsLeaderboard(
    first: GBPlayerStat?,
    second: GBPlayerStat?,
    third: GBPlayerStat?,
    onPlayerSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween
    ) {
        LeaderboardPlayer(
            modifier = Modifier.weight(1f),
            player = second,
            position = LeaderboardPosition.SECOND,
            onPlayerSelected = onPlayerSelected
        )
        LeaderboardPlayer(
            modifier = Modifier.weight(1f), 
            player = first,
            position = LeaderboardPosition.FIRST,
            onPlayerSelected = onPlayerSelected
        )
        LeaderboardPlayer(
            modifier = Modifier.weight(1f), 
            player = third,
            position = LeaderboardPosition.THIRD,
            onPlayerSelected = onPlayerSelected
        )
    }
}

@Composable
private fun LeaderboardPlayer(
    modifier: Modifier,
    player: GBPlayerStat?,
    position: LeaderboardPosition,
    onPlayerSelected: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(4.dp)
    ) {
        LeaderName(
            player = player
        )
        LeaderCard(
            player = player,
            position = position,
            onClick = { player?.id?.let(onPlayerSelected) }
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = player?.value.orEmpty(),
            style = gBTypography().bodySmall,
            alignment = TextAlign.Center,
            textColor = elevated_button_bg_not_selected
        )
    }
}

@Composable
private fun LeaderName(
    player: GBPlayerStat?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Center
    ) {
        GBText(
            text = player?.name.orEmpty(),
            style = gBTypography().bodySmall,
            alignment = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.width(4.dp)
        )
        PositionArrow(
            classificationChanged = player?.changedPosition ?: 0,
            arrowFirst = true
        )
    }
}

@Composable
private fun LeaderCard(
    player: GBPlayerStat?,
    position: LeaderboardPosition,
    onClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    val imageModifier = if (isLoading) {
        Modifier.clip(RoundedCornerShape(50)).shimmerEffect()
    } else {
        Modifier.clip(RoundedCornerShape(50))
    }

    Box(
        modifier = Modifier
            .size(position.circleSize + LEADER_POSITION_SIZE / 2)
    ) {
        Box(
            modifier = imageModifier
                .align(TopCenter)
                .border(width = 2.dp, color = position.circleColor, shape = RoundedCornerShape(50))
        ) {
            GBAsyncImage(
                modifier = Modifier
                    .size(position.circleSize)
                    .clickable { onClick() }
                    .padding(8.dp),
                image = player?.bodyImage,
                contentScale = Fit,
                placeholder = AppImages.facePlayer,
                isLoading = false,
                finishLoading = { isLoading = false }
            )
        }
        Box(
            modifier = Modifier
                .size(LEADER_POSITION_SIZE)
                .align(BottomCenter)
                .clip(RoundedCornerShape(50))
                .background(position.circleColor),
            contentAlignment = Alignment.Center
        ) {
            GBText(
                text = position.number.toString(),
                textColor = Black,
                alignment = TextAlign.Center,
                style = gBTypography().bodyMedium
            )
        }
    }
}

@Composable
internal fun PositionArrow(
    classificationChanged: Int,
    arrowFirst: Boolean
) {
    if (classificationChanged == 0) return

    val color = if (classificationChanged > 0) softGreen else softRed
    val arrow = if (classificationChanged > 0) Res.drawable.ic_arrow_up else Res.drawable.ic_arrow_down

    val icon = @Composable {
        GBIcon(
            modifier = Modifier.size(12.dp),
            icon = painterResource(arrow),
            tint = color
        )
    }
    val amount = @Composable {
        GBText(
            text = classificationChanged.absoluteValue.toString(),
            textColor = color,
            style = gBTypography().bodySmall.copy(fontSize = 10.sp)
        )
    }

    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(if (arrowFirst) 4.dp else 8.dp)
    ) {
        if (arrowFirst) {
            icon()
            amount()
        } else {
            amount()
            icon()
        }
    }
}
