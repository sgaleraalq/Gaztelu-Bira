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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState.Companion.INSERTABLE_STATS
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.Stat
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.STATS
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_stats
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_stats_subtitle
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_stats_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Who did what. Each stat is a list of players: adding the same one again counts it again, so two
 * goals are the scorer listed twice.
 */
@Composable
internal fun MatchStats(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val enabled = state.state.isNotLoading()
    MatchCard {
        MatchSectionHeader(
            icon = Res.drawable.ic_stats,
            iconColor = primaryRed,
            title = stringResource(Res.string.insert_match_stats_title),
            subtitle = stringResource(Res.string.insert_match_stats_subtitle),
            expanded = STATS in state.expanded,
            onClick = { actions.toggleSection(STATS) }
        )
        AnimatedVisibility(visible = STATS in state.expanded) {
            Column {
                INSERTABLE_STATS.forEach { stat ->
                    HorizontalDivider(color = matchCardBorder)
                    MatchStat(
                        stat = stat,
                        players = state.stats[stat].orEmpty(),
                        enabled = enabled,
                        actions = actions
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MatchStat(
    stat: GBStat,
    players: List<GBPlayer>,
    enabled: Boolean,
    actions: InsertMatchActions
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier.size(22.dp),
                painter = painterResource(stat.icon),
                contentDescription = null,
                tint = Unspecified
            )
            GBText(
                modifier = Modifier.weight(1f),
                text = stringResource(stat.label),
                style = GBTheme.typography.bodyMedium
            )
            if (players.isNotEmpty()) {
                GBText(
                    text = players.size.toString(),
                    textColor = matchMutedText,
                    style = GBTheme.typography.titleSmall.copy(fontWeight = Bold)
                )
            }
            MatchAddButton(
                enabled = enabled,
                onClick = { actions.updateDialogState(InsertMatchDialog.Player(Stat(stat))) }
            )
        }
        if (players.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = spacedBy(8.dp),
                verticalArrangement = spacedBy(8.dp)
            ) {
                players.forEachIndexed { index, player ->
                    MatchPlayerChip(
                        player = player,
                        enabled = enabled,
                        onRemove = { actions.removePlayer(Stat(stat), index) }
                    )
                }
            }
        }
    }
}
