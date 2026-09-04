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

package com.sgale.gaztelubira.core.screens.insert_match.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextOverflow.Companion.Visible
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.player.Stat.Assists
import com.sgale.gaztelubira.core.domain.model.player.Stat.CleanSheets
import com.sgale.gaztelubira.core.domain.model.player.Stat.Fails
import com.sgale.gaztelubira.core.domain.model.player.Stat.GamesPlayed
import com.sgale.gaztelubira.core.domain.model.player.Stat.Goals
import com.sgale.gaztelubira.core.domain.model.player.Stat.GoalsProvoked
import com.sgale.gaztelubira.core.domain.model.player.Stat.PenaltiesProvoked
import com.sgale.gaztelubira.core.domain.model.player.Stat.Percentage
import com.sgale.gaztelubira.core.domain.model.player.Stat.RedCards
import com.sgale.gaztelubira.core.domain.model.player.Stat.Saves
import com.sgale.gaztelubira.core.domain.model.player.Stat.YellowCards
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchStats
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_text_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import androidx.compose.ui.res.painterResource
import com.sgale.gaztelubira.core.screens.toGBPlayer

private val STAT_BOX_SIZE = 75.dp
private val PLAYER_STAT_BOX_SIZE = 20.dp

@Composable
internal fun InsertMatchStats(
    modifier: Modifier,
    statsState: InsertMatchStats,
    changePlayerState: () -> Unit,
    showPlayers: () -> Unit,
    selectStat: (Stat) -> Unit,
    removeStat: (Int, Stat) -> Unit
) {
    var showStat by remember { mutableStateOf<Stat?>(null) }
    val insertableStats = Stat.entries.filter { it.isInsertable }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = STAT_BOX_SIZE),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(insertableStats) { stat ->
            val values = getPlayerStats(statsState, stat)
            InsertMatchStatBox(
                stat = stat,
                values = values,
                changePlayerState = { changePlayerState() },
                showStat = { showStat = stat; selectStat(stat) }
            )
        }
    }

    if (showStat != null) {
        GBDialog(
            dismiss = { showStat = null }
        ) {
            val currentStat = showStat!!
            InsertMatchStatPlayersBigBox(
                players = getPlayerStats(statsState, currentStat),
                stat = currentStat,
                changePlayerState = { changePlayerState() },
                showPlayers = { showPlayers() },
                removeStat = { idx -> removeStat(idx, currentStat) }
            )
        }
    }
}

@Composable
internal fun InsertMatchStatBox(
    stat: Stat,
    values: List<PlayerModel>,
    changePlayerState: () -> Unit,
    showStat: () -> Unit
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        StatBox(
            stat = stat,
            values = values,
            changePlayerState = { changePlayerState() },
            showStat = { showStat() }
        )
        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(stat.statName),
            style = gBTypography().bodySmall.copy(fontSize = 10.sp),
            overflow = Visible,
            alignment = Center
        )
    }
}

@Composable
internal fun StatBox(
    stat: Stat,
    values: List<PlayerModel>,
    changePlayerState: () -> Unit,
    showStat: () -> Unit
){
    Box(
        modifier = Modifier.size(STAT_BOX_SIZE)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = elevated_button_text_color
            ),
            onClick = { changePlayerState(); showStat() }
        ) {
            if (values.isEmpty()) {
                GBIcon(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    icon = painterResource(stat.icon),
                    size = 50.dp,
                    tint = Unspecified
                )
            } else {
                InsertMatchStatPlayersSmallBox(values)
            }
        }

        if (values.isNotEmpty()) {
            NumberOfStatsInsideContainer(Modifier.align(TopEnd), values.size)
        }
    }
}

@Composable
internal fun InsertMatchStatPlayersSmallBox(
    playerStats: List<PlayerModel>
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.FixedSize(PLAYER_STAT_BOX_SIZE),
        contentPadding = PaddingValues(2.dp),
        horizontalArrangement = spacedBy(4.dp),
        verticalArrangement = spacedBy(4.dp)
    ) {
        items(
            playerStats.distinctBy { it.id }.take(9)
        ) { player ->
            GBPlayerCard(
                modifier = Modifier.size(PLAYER_STAT_BOX_SIZE),
                player = player.toGBPlayer(),
                isClickable = false,
                showDorsal = false
            )
        }
    }
}

@Composable
internal fun NumberOfStatsInsideContainer(
    modifier: Modifier,
    stats: Int
){
    GBText(
        modifier = modifier
            .clip(CircleShape)
            .background(White)
            .padding(4.dp),
        text = stats.toString(),
        textColor = Black,
        style = gBTypography().bodySmall.copy(
            fontSize = 10.sp
        )
    )
}

@Composable
internal fun InsertMatchStatPlayersBigBox(
    players: List<PlayerModel>,
    stat: Stat,
    changePlayerState: () -> Unit,
    showPlayers: () -> Unit,
    removeStat: (Int) -> Unit
) {
    val counts = remember(players) {
        players.groupingBy { it.name }.eachCount()
    }
    val uniquePlayers = remember(players) {
        players.distinctBy { it.name }
    }

    LazyVerticalGrid(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(gray_box_in_black_bg)
            .size(500.dp),
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(12.dp)
    ) {
        item {
            StatsTitle(stat)
        }
        itemsIndexed(uniquePlayers) { idx, player ->
            val count = counts[player.name] ?: 1
            InsertMatchPlayerStat(
                player = player,
                count = count,
                changePlayerState = { changePlayerState() },
                removeStat = { removeStat(idx) }
            )
        }
        item {
            GBElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(stat.statTitle ?: R.string.insert_player),
                enabled = true,
                onClick = { showPlayers() }
            )
        }
    }
}

@Composable
internal fun StatsTitle(stat: Stat){
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GBIcon(icon = painterResource(stat.icon))
            Spacer(Modifier.width(16.dp))
            GBText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(stat.statName),
                style = gBTypography().titleLarge,
                textColor = elevated_button_text_color
            )
        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
    }
}

@Composable
internal fun InsertMatchPlayerStat(
    player: PlayerModel,
    count: Int,
    changePlayerState: () -> Unit,
    removeStat: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        GBText(
            text = count.toString()
        )
        GBPlayerImage(
            modifier = Modifier.size(50.dp),
            image = player.faceImage,
        )
        GBText(
            modifier = Modifier.weight(1f),
            text = player.name,
            style = gBTypography().copy().bodyMedium
        )
        GBIcon(
            modifier = Modifier.clickable{ changePlayerState(); removeStat() },
            icon = painterResource(R.drawable.ic_delete)
        )
    }
}

private fun getPlayerStats(state: InsertMatchStats, stat: Stat): List<PlayerModel> {
    return when (stat) {
        Assists -> state.assists
        CleanSheets -> state.cleanSheets
        Fails -> state.fails
        Goals -> state.goals
        GoalsProvoked -> state.goalsProvoked
        PenaltiesProvoked -> state.penaltiesProvoked
        RedCards -> state.redCards
        Saves -> state.saves
        YellowCards -> state.yellowCards
        GamesPlayed, Percentage -> emptyList()
    }
}
