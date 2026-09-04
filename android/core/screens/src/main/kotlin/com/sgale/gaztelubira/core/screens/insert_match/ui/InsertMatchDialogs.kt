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

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Position.Manager
import com.sgale.gaztelubira.core.domain.model.player.Stat.CleanSheets
import com.sgale.gaztelubira.core.domain.model.player.Stat.RedCards
import com.sgale.gaztelubira.core.domain.model.player.Stat.YellowCards
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchFormation
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchStats
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.screens.toGBPlayer
import com.sgale.gaztelubira.multiplatform.designsystem.model.label

private val CARD_SIZE = 60.dp

@Composable
internal fun InsertMatchDialogPlayers(
    playerPosition: LineUpPosition,
    isManager: Boolean,
    players: List<PlayerModel>,
    formation: InsertMatchFormation,
    onPlayerSelected: (PlayerModel) -> Unit,
    dismiss: () -> Unit
) {
    val usedIds: Set<String> = buildSet {
        formation.lineUp.values.filterNotNull().forEach { add(it.id) }
        formation.benchPlayers.forEach { add(it.id) }
        formation.managers.first?.let { add(it.id) }
        formation.managers.second?.let { add(it.id) }
    }
    val availablePlayers = players.filter { it.id !in usedIds }

    GBDialog(
        modifier = Modifier.fillMaxSize(),
        dismiss = { dismiss() }
    ) { modifier ->
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(minSize = CARD_SIZE),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = spacedBy(8.dp),
            verticalArrangement = spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                GBText(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    text = stringResource(R.string.select_a_player) + ": " + playerPosition.label,
                    alignment = Center,
                    style = gBTypography().titleMedium
                )
            }
            items(
                availablePlayers.filter {
                    if (isManager) {
                        it.position == Manager
                    } else {
                        it.position != Manager
                    }
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = spacedBy(8.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    GBPlayerCard(
                        modifier = Modifier.size(CARD_SIZE),
                        player = it.toGBPlayer(),
                        onPlayerClicked = { dismiss(); onPlayerSelected(it) },
                        showDorsal = !isManager,
                        dorsalSize = 16.dp,
                        dorsalInternalPadding = 0.dp,
                        dorsalTextSize = 8.sp,
                        allowLongTap = false
                    )
                    GBText(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.name,
                        style = gBTypography().bodySmall,
                        alignment = Center
                    )
                }
            }
        }
    }
}

@Composable
fun InsertMatchDialogStatsPlayers(
    statsState: InsertMatchStats?,
    players: List<PlayerModel>,
    formationState: InsertMatchFormation,
    onPlayerSelected: (PlayerModel) -> Unit,
    dismiss: () -> Unit
){
    val playersInLineup = formationState.lineUp.values.filterNotNull()
    val playersInBench = formationState.benchPlayers

    val availablePlayers = players
        .filter { it.position != Manager }
        .filter { it in playersInLineup || it in playersInBench }
        .filter { player ->
            when (statsState?.selectedStat) {
                CleanSheets -> player !in statsState.cleanSheets
                YellowCards -> statsState.yellowCards.count { it.id == player.id } < 2
                RedCards -> player !in statsState.redCards
                else -> true
            }
        }

    GBDialog(
        modifier = Modifier.fillMaxSize(),
        dismiss = { dismiss() }
    ) { modifier ->
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(minSize = CARD_SIZE),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = spacedBy(8.dp),
            verticalArrangement = spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                GBText(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    text = stringResource(statsState?.selectedStat?.statName ?: R.string.goals),
                    alignment = Center,
                    style = gBTypography().titleMedium
                )
            }
            items(availablePlayers) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = spacedBy(8.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    GBPlayerCard(
                        modifier = Modifier.size(CARD_SIZE),
                        player = it.toGBPlayer(),
                        onPlayerClicked = { dismiss(); onPlayerSelected(it) },
                        dorsalSize = 16.dp,
                        dorsalInternalPadding = 0.dp,
                        dorsalTextSize = 8.sp,
                        allowLongTap = false
                    )
                    GBText(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.name,
                        style = gBTypography().bodySmall,
                        alignment = Center
                    )
                }
            }
        }
    }
}
