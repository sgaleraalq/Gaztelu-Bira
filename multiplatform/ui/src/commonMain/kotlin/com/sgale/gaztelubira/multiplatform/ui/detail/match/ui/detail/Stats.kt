/*
 * Designed and developed by 2026 sgale (Sergio Galera)
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

package com.sgale.gaztelubira.multiplatform.ui.detail.match.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.white_in_gray_box
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.ASSISTS
import com.sgale.gaztelubira.multiplatform.model.GBStat.BLUE_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.CLEAN_SHEETS
import com.sgale.gaztelubira.multiplatform.model.GBStat.FAILS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GAMES_PLAYED
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBStat.PENALTIES_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBStat.PERCENTAGE
import com.sgale.gaztelubira.multiplatform.model.GBStat.RED_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.SAVES
import com.sgale.gaztelubira.multiplatform.model.GBStat.YELLOW_CARDS
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Stats
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailStats
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_down
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_right
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MatchDetailStateStats(
    modifier: Modifier,
    state: MatchDetailState?
) {
    val statsState = state as? Stats ?: return
    val matchStats = statsState.stats ?: return
    val statPlayers = GBStat.entries
        .map { stat -> stat to matchStats.playersFor(stat) }
        .filter { (_, players) -> players.isNotEmpty() }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(statPlayers, key = { (stat, _) -> stat.name }) { (stat, players) ->
            StatDropdown(
                stat = stat,
                players = players
            )
        }
    }
}

/**
 * [PERCENTAGE] and [GAMES_PLAYED] are season-wide stats, so a single match has no players for them.
 */
private fun MatchDetailStats.playersFor(stat: GBStat): List<GBPlayer> =
    when (stat) {
        GOALS -> goals
        GOALS_PROVOKED -> goalsProvoked
        ASSISTS -> assists
        CLEAN_SHEETS -> cleanSheets
        PENALTIES_PROVOKED -> penaltiesProvoked
        SAVES -> saves
        FAILS -> fails
        YELLOW_CARDS -> yellowCards
        RED_CARDS -> redCards
        BLUE_CARDS -> blueCards
        PERCENTAGE, GAMES_PLAYED -> emptyList()
    }

@Composable
private fun StatDropdown(
    stat: GBStat,
    players: List<GBPlayer>,
) {
    var expanded by rememberSaveable(stat) { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {
        StatHeader(
            stat = stat,
            expanded = expanded,
            onClick = { expanded = !expanded }
        )
        HorizontalDivider()

        if (expanded) {
            players.forEach { player ->
                RowPlayer(player = player)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StatHeader(
    stat: GBStat,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(benchBgColor)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        GBIcon(
            icon = painterResource(stat.icon),
        )
        GBText(
            modifier = Modifier.weight(1f),
            text = stringResource(stat.label),
            style = gBTypography().bodyLarge.copy(fontWeight = Bold),
            textColor = white_in_gray_box
        )
        GBIcon(
            modifier = Modifier.size(12.dp),
            icon = painterResource(if (expanded) Res.drawable.ic_arrow_down else Res.drawable.ic_arrow_right),
            tint = White
        )
    }
}

@Composable
private fun RowPlayer(
    modifier: Modifier = Modifier,
    player: GBPlayer
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = benchHorizontalPadding)
            .padding(vertical = 8.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        GBImage(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(50)),
            image = player.image,
            placeholder = AppImages.facePlayer
        )
        GBText(
            text = player.name,
            textColor = white_in_gray_box,
            style = gBTypography().bodyMedium
        )
    }
}
