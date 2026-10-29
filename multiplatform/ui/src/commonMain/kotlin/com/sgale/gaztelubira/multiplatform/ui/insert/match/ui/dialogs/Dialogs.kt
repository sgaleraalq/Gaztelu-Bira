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

package com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBIcon
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerImage
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTeam
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTeamName
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpPosition
import com.sgale.gaztelubira.multiplatform.designsystem.model.label
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_text_color
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.None
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.Players
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.Stat
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.StatPlayers
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.Teams
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.AddPlayer
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.AddStat
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.RemoveStat
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Rival
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.Bench
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.LineUp
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.Manager
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_delete
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_add_player
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_select_a_player
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_select_a_team
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val CARD_SIZE = 60.dp
private const val TEAM_COLUMNS = 4

@Composable
internal fun InsertMatchDialogs(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val dismiss = { actions.updateDialog(None) }

    when (val dialog = state.dialog) {
        None -> Unit

        Teams -> TeamsDialog(
            state = state,
            dismiss = dismiss,
            onTeamSelected = { team ->
                actions.updateField(Rival(team))
                dismiss()
            }
        )

        is Players -> PlayersDialog(
            title = "${stringResource(Res.string.insert_match_select_a_player)}: ${dialog.slot.label}",
            players = state.availableFor(dialog.slot),
            showDorsal = dialog.slot != Manager,
            dismiss = dismiss,
            onPlayerSelected = { player ->
                actions.updateField(AddPlayer(dialog.slot, player))
                dismiss()
            }
        )

        is Stat -> StatDialog(
            stat = dialog.stat,
            players = state.stat(dialog.stat),
            dismiss = dismiss,
            onRemove = { player -> actions.updateField(RemoveStat(dialog.stat, player)) },
            onAdd = { actions.updateDialog(StatPlayers(dialog.stat)) }
        )

        /**
         * Back to the stat once picked, so several can go in a row.
         */
        is StatPlayers -> PlayersDialog(
            title = stringResource(dialog.stat.label),
            players = state.availableFor(dialog.stat),
            showDorsal = true,
            dismiss = { actions.updateDialog(Stat(dialog.stat)) },
            onPlayerSelected = { player ->
                actions.updateField(AddStat(dialog.stat, player))
                actions.updateDialog(Stat(dialog.stat))
            }
        )
    }
}

private val PlayerSlot.label: String
    @Composable get() = when (this) {
        is LineUp -> position.label
        Bench -> LineUpPosition.Bench.label
        Manager -> LineUpPosition.Manager.label
    }

@Composable
private fun TeamsDialog(
    state: InsertMatchUiState,
    dismiss: () -> Unit,
    onTeamSelected: (GBTeam) -> Unit
) {
    GBDialog(dismiss = dismiss) { modifier ->
        LazyVerticalGrid(
            modifier = modifier.padding(16.dp),
            columns = GridCells.Fixed(TEAM_COLUMNS),
            verticalArrangement = spacedBy(16.dp),
            horizontalArrangement = spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(TEAM_COLUMNS) }) {
                GBText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.insert_match_select_a_team),
                    alignment = Center,
                    style = GBTheme.typography.headlineSmall
                )
            }
            items(state.rivals, key = { it.id }) { team ->
                Column(
                    modifier = Modifier.clickable { onTeamSelected(team) },
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    GBTeam(
                        modifier = Modifier.size(50.dp),
                        image = team.logo,
                        placeholder = AppImages.teamCrest
                    )
                    GBTeamName(
                        name = team.name,
                        style = GBTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayersDialog(
    title: String,
    players: List<GBPlayer>,
    showDorsal: Boolean,
    dismiss: () -> Unit,
    onPlayerSelected: (GBPlayer) -> Unit
) {
    GBDialog(
        modifier = Modifier.fillMaxSize(),
        dismiss = dismiss
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
                    text = title,
                    alignment = Center,
                    style = GBTheme.typography.titleMedium
                )
            }
            items(players, key = { it.id }) { player ->
                Column(
                    verticalArrangement = spacedBy(8.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    GBPlayerCard(
                        modifier = Modifier.size(CARD_SIZE),
                        player = player,
                        placeholder = AppImages.facePlayer,
                        onPlayerClicked = { onPlayerSelected(player) },
                        showDorsal = showDorsal,
                        dorsalSize = 16.dp,
                        dorsalInternalPadding = 0.dp,
                        dorsalTextSize = 8.sp
                    )
                    GBText(
                        modifier = Modifier.fillMaxWidth(),
                        text = player.name,
                        style = GBTheme.typography.bodySmall,
                        alignment = Center
                    )
                }
            }
        }
    }
}

/**
 * One row per player with how many times they got [stat]; removing takes away one of them.
 */
@Composable
private fun StatDialog(
    stat: GBStat,
    players: List<GBPlayer>,
    dismiss: () -> Unit,
    onRemove: (GBPlayer) -> Unit,
    onAdd: () -> Unit
) {
    val counts = players.groupingBy { it.id }.eachCount()

    GBDialog(dismiss = dismiss) { modifier ->
        LazyVerticalGrid(
            modifier = modifier.size(500.dp),
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = spacedBy(12.dp)
        ) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = CenterVertically
                    ) {
                        GBIcon(icon = painterResource(stat.icon))
                        Spacer(Modifier.width(16.dp))
                        GBText(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(stat.label),
                            style = GBTheme.typography.titleLarge,
                            textColor = elevated_button_text_color
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                }
            }
            items(players.distinctBy { it.id }, key = { it.id }) { player ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = spacedBy(16.dp)
                ) {
                    GBText(text = counts[player.id]?.toString())
                    GBPlayerImage(
                        modifier = Modifier.size(50.dp),
                        image = player.image,
                        placeholder = AppImages.facePlayer
                    )
                    GBText(
                        modifier = Modifier.weight(1f),
                        text = player.name,
                        style = GBTheme.typography.bodyMedium
                    )
                    GBIcon(
                        modifier = Modifier.clickable { onRemove(player) },
                        icon = painterResource(Res.drawable.ic_delete)
                    )
                }
            }
            item {
                GBElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.insert_match_add_player),
                    onClick = onAdd
                )
            }
        }
    }
}
