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

package com.sgale.gaztelubira.multiplatform.ui.insert.match.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBAddButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBFootballField
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBPlayerCard
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.FIVE_THREE_TWO
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.FOUR_FOUR_TWO
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.FOUR_THREE_THREE
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_text_color
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.match.BENCH_LIMIT
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.MANAGERS_LIMIT
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.Players
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Formation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.RemovePlayer
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.Bench
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.LineUp
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.PlayerSlot.Manager
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.common_bench
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_bench_player
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_managers
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val BENCH_PLAYER_SIZE = 70.dp

@Composable
internal fun InsertMatchFormation(
    modifier: Modifier,
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    Column(modifier = modifier) {
        Formations(
            selected = state.formation,
            onFormationSelected = { actions.updateField(Formation(it)) }
        )
        GBFootballField(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            showAnimation = true,
            players = state.lineUp,
            fieldImage = AppImages.footballField,
            playerPlaceholder = AppImages.facePlayer,
            formation = state.formation,
            onPlayerSelected = { position, index ->
                actions.updateDialog(Players(LineUp(index, position)))
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(12.dp)
        ) {
            CalledUp(
                modifier = Modifier.weight(1f),
                title = Res.string.common_bench,
                players = state.bench,
                limit = BENCH_LIMIT,
                slot = Bench,
                actions = actions
            )
            VerticalDivider(Modifier.height(BENCH_PLAYER_SIZE + 10.dp))
            CalledUp(
                modifier = Modifier.weight(0.6f),
                title = Res.string.insert_match_managers,
                players = state.managers,
                limit = MANAGERS_LIMIT,
                slot = Manager,
                actions = actions
            )
        }
    }
}

@Composable
private fun Formations(
    selected: LineUpFormation,
    onFormationSelected: (LineUpFormation) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(
            items = LineUpFormation.entries,
            key = { it.name }
        ) { formation ->
            val isSelected = formation == selected
            GBElevatedButton(
                text = formation.label(),
                onClick = { onFormationSelected(formation) },
                backgroundColor = if (isSelected) elevated_button_bg else elevated_button_bg_not_selected,
                textColor = if (isSelected) elevated_button_text_color else elevated_button_bg
            )
        }
    }
}

/**
 * The bench or the managers: whoever is in it, removable, and a way to add one more while there is
 * room.
 */
@Composable
private fun CalledUp(
    modifier: Modifier,
    title: StringResource,
    players: List<GBPlayer>,
    limit: Int,
    slot: PlayerSlot,
    actions: InsertMatchActions
) {
    val pickPlayer = { actions.updateDialog(Players(slot)) }

    Column(modifier) {
        GBText(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            text = stringResource(title),
            style = GBTheme.typography.bodyLarge
        )
        if (players.isEmpty() && slot == Bench) {
            Box(Modifier.height(BENCH_PLAYER_SIZE), contentAlignment = Center) {
                GBElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.insert_match_bench_player),
                    onClick = pickPlayer
                )
            }
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth().height(BENCH_PLAYER_SIZE),
                verticalAlignment = CenterVertically,
                horizontalArrangement = spacedBy(12.dp)
            ) {
                items(
                    items = players,
                    key = { it.id }
                ) { player ->
                    GBPlayerCard(
                        modifier = Modifier.size(BENCH_PLAYER_SIZE),
                        player = player,
                        placeholder = AppImages.facePlayer,
                        showDorsal = false,
                        showDeletion = true,
                        onPlayerRemoved = { actions.updateField(RemovePlayer(slot, player)) }
                    )
                }
                if (players.size < limit) {
                    item {
                        GBAddButton(
                            show = true,
                            onButtonClicked = pickPlayer
                        )
                    }
                }
            }
        }
    }
}

private fun LineUpFormation.label(): String =
    when (this) {
        FOUR_THREE_THREE -> "4-3-3"
        FOUR_FOUR_TWO -> "4-4-2"
        FIVE_THREE_TWO -> "5-3-2"
    }
