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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.End
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.PlayerDisplayStats
import com.sgale.gaztelubira.core.screens.home.tabs.stats.displayStat
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBAsyncImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.softGreen
import com.sgale.gaztelubira.multiplatform.designsystem.style.softRed
import kotlin.math.absoluteValue

private val PLAYER_CLASSIFICATION_SIZE = 36.dp

@Composable
internal fun StatsClassification(
    modifier: Modifier,
    players: List<PlayerDisplayStats>,
    selectedStat: Stat,
    selectPlayer: (FirebaseId) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(
            players
                .drop(3)
                .filter { it.player.dorsal != null }
        ) { index, player ->
            ClassificationCard(
                player = player,
                position = index + 4,
                selectedStat = selectedStat,
                onClick = { selectPlayer(player.id) }
            )
        }

        itemsIndexed(
            players
                .filter { it.player.dorsal == null }
        ) { _, player ->
            NullPlayer(player)
        }
    }
}

@Composable
private fun ClassificationCard(
    player: PlayerDisplayStats,
    position: Int,
    selectedStat: Stat,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp)
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        ClassificationPosition(position.toString())
        ClassificationPlayerImage(player.player)
        ClassificationName(Modifier.weight(1f), player)
        ClassificationArrow(player.changedPosition)
        ClassificationStat(selectedStat, player.stat)
    }
}

@Composable
private fun ClassificationPosition(
    position: String?
) {
    GBText(
        modifier = Modifier.width(24.dp),
        text = position ?: "",
        style = gBTypography().bodyMedium,
        alignment = End
    )
}

@Composable
private fun ClassificationPlayerImage(
    player: PlayerModel
) {
    var isLoading by remember { mutableStateOf(true) }
    GBAsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(White)
            .size(PLAYER_CLASSIFICATION_SIZE),
        image = player.faceImage,
        contentScale = Fit,
        isLoading = isLoading,
        finishLoading = { isLoading = false }
    )
}

@Composable
private fun ClassificationName(
    modifier: Modifier,
    player: PlayerDisplayStats
) {
    val position = player.player.position
    val positionName = if (position != null) {
        stringResource(position.positionName)
    } else {
        null
    }
    val playerDorsal = if (player.player.dorsal == null) "" else "${player.player.dorsal}. "
    Column(
        modifier = modifier,
    ) {
        GBText(
            text = playerDorsal + player.player.name,
            style = gBTypography().bodySmall
        )
        positionName?.let {
            GBText(
                text = it,
                textColor = elevated_button_bg_not_selected,
                style = gBTypography().bodySmall.copy(
                    fontSize = 10.sp,
                    fontStyle = Italic
                )
            )
        }
    }
}

@Composable
private fun ClassificationArrow(
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
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        GBText(
            text = classificationChanged.absoluteValue.toString(),
            textColor = color,
            style = gBTypography().bodySmall.copy(
                fontSize = 10.sp
            )
        )
        GBIcon(
            modifier = Modifier.size(12.dp),
            icon = arrow,
            tint = color
        )
    }
}

@Composable
private fun ClassificationStat(
    selectedStat: Stat,
    stat: Double
) {
    GBText(
        modifier = Modifier.width(32.dp),
        text = displayStat(selectedStat, stat),
        alignment = Center
    )
}

@Composable
private fun NullPlayer(
    player: PlayerDisplayStats
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Gray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 6.dp)
            .padding(end = 12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        ClassificationPosition(null)
        ClassificationPlayerImage(player.player)
        ClassificationName(Modifier.weight(1f), player)
    }
}
