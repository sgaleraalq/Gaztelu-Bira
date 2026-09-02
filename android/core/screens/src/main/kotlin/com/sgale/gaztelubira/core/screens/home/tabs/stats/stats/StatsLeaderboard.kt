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

package com.sgale.gaztelubira.core.screens.home.tabs.stats.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.core.designsystem.components.GBAsyncImage
import com.sgale.gaztelubira.core.designsystem.components.GBIcon
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.components.getSaverImage
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.leaderboardFirst
import com.sgale.gaztelubira.multiplatform.designsystem.style.leaderboardSecond
import com.sgale.gaztelubira.multiplatform.designsystem.style.leaderboardThird
import com.sgale.gaztelubira.multiplatform.designsystem.style.softGreen
import com.sgale.gaztelubira.multiplatform.designsystem.style.softRed
import com.sgale.gaztelubira.core.designsystem.utils.shimmerEffect
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.PlayerDisplayStats
import com.sgale.gaztelubira.core.screens.home.tabs.stats.displayStat
import kotlin.math.absoluteValue

private val LEADER_POSITION_SIZE = 24.dp

@Composable
internal fun StatsLeaderboard(
    selectedStat: Stat,
    first: PlayerDisplayStats?,
    second: PlayerDisplayStats?,
    third: PlayerDisplayStats?,
    selectPlayer: (FirebaseId) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween
    ) {
        LeaderboardPlayer(
            modifier = Modifier.weight(1f),
            selectedStat = selectedStat,
            size = 75.dp,
            player = second,
            number = 2,
            selectPlayer = { selectPlayer(it) },
            color = leaderboardSecond
        )
        LeaderboardPlayer(
            modifier = Modifier.weight(1f),
            selectedStat = selectedStat,
            size = 100.dp,
            player = first,
            number = 1,
            selectPlayer = { selectPlayer(it) },
            color = leaderboardFirst
        )
        LeaderboardPlayer(
            modifier = Modifier.weight(1f),
            selectedStat = selectedStat,
            size = 65.dp,
            player = third,
            number = 3,
            selectPlayer = { selectPlayer(it) },
            color = leaderboardThird
        )
    }
}

@Composable
private fun LeaderboardPlayer(
    modifier: Modifier,
    selectedStat: Stat,
    size: Dp,
    player: PlayerDisplayStats?,
    number: Int,
    selectPlayer: (FirebaseId) -> Unit,
    color: Color = White
) {

    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(4.dp)
    ) {
        LeaderName(player)
        LeaderCard(
            size = size,
            player = player,
            number = number,
            color = color,
            onClick = { selectPlayer(player?.id ?: "") }
        )
        Spacer(Modifier.height(4.dp))
        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = displayStat(selectedStat, player?.stat),
            style = gBTypography().bodySmall,
            alignment = Center,
            textColor = elevated_button_bg_not_selected
        )
    }
}

@Composable
fun LeaderName(
    player: PlayerDisplayStats?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GBText(
            text = player?.player?.name ?: "",
            style = gBTypography().bodySmall,
            alignment = Center
        )
        Spacer(Modifier.width(4.dp))
        LeaderArrow(
            classificationChanged = player?.changedPosition ?: 0
        )
    }
}

@Composable
fun LeaderCard(
    size: Dp,
    player: PlayerDisplayStats?,
    number: Int,
    color: Color,
    onClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    val modifier = if (isLoading) {
        Modifier.clip(RoundedCornerShape(50)).shimmerEffect()
    } else {
        Modifier.clip(RoundedCornerShape(50))
    }

    Box(
        modifier = Modifier.size(size + LEADER_POSITION_SIZE / 2)
    ) {
        Box(
            modifier = modifier
                .align(TopCenter)
                .border(
                    width = 2.dp,
                    color = color,
                    shape = RoundedCornerShape(50)
                )
        ) {
            GBAsyncImage(
                modifier = Modifier
                    .size(size)
                    .clickable{ onClick() }
                    .padding(8.dp),
                image = player?.player?.bodyImage,
                contentScale = Fit,
                saverImage = getSaverImage(player?.player?.position),
                isLoading = false,
                finishLoading = { isLoading = false }
            )
        }
        Box(
            modifier = Modifier
                .size(LEADER_POSITION_SIZE)
                .align(BottomCenter)
                .clip(RoundedCornerShape(50))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            GBText(
                text = number.toString(),
                textColor = Black,
                alignment = Center,
                style = gBTypography().bodyMedium
            )
        }
    }
}

@Composable
private fun LeaderArrow(
    modifier: Modifier = Modifier,
    classificationChanged: Int
) {
    if (classificationChanged == 0) return

    val color = when {
        classificationChanged > 0 -> softGreen
        else -> softRed
    }

    val arrow = when {
        classificationChanged > 0 -> R.drawable.ic_arrow_up
        else -> R.drawable.ic_arrow_down
    }

    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(4.dp)
    ) {
        GBIcon(
            modifier = Modifier.size(12.dp),
            icon = arrow,
            tint = color
        )
        GBText(
            text = classificationChanged.absoluteValue.toString(),
            textColor = color,
            style = gBTypography().bodySmall.copy(
                fontSize = 10.sp
            )
        )
    }
}
