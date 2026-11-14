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
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBFootballField
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.FIVE_THREE_TWO
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.FOUR_FOUR_TWO
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.FOUR_THREE_THREE
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Formation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.Bench
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.LineUp
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchPlayerTarget.Manager
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.LINE_UP
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_formation
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_bench_count
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_formation
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_line_up_subtitle
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_line_up_title
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_staff
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_staff_count
import org.jetbrains.compose.resources.stringResource

private const val PITCH_ASPECT_RATIO = 0.8f

private val lineUpColor = Color(0xFF2FBF8F)

@Composable
internal fun MatchLineUp(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val enabled = state.state.isNotLoading()
    MatchCard {
        MatchSectionHeader(
            icon = Res.drawable.ic_formation,
            iconColor = lineUpColor,
            title = stringResource(Res.string.insert_match_line_up_title),
            subtitle = stringResource(Res.string.insert_match_line_up_subtitle),
            expanded = LINE_UP in state.expanded,
            onClick = { actions.toggleSection(LINE_UP) }
        )
        AnimatedVisibility(visible = LINE_UP in state.expanded) {
            Column {
                HorizontalDivider(color = matchCardBorder)
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = spacedBy(16.dp)
                ) {
                    MatchFormations(state, enabled, actions)
                    MatchPitch(state, enabled, actions)
                    MatchCallUp(
                        title = stringResource(Res.string.insert_match_bench_count, state.bench.size),
                        subtitle = null,
                        players = state.bench,
                        target = Bench,
                        enabled = enabled,
                        actions = actions
                    )
                    MatchCallUp(
                        title = stringResource(Res.string.insert_match_staff),
                        subtitle = stringResource(Res.string.insert_match_staff_count, state.managers.size),
                        players = state.managers,
                        target = Manager,
                        enabled = enabled,
                        actions = actions
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchFormations(
    state: InsertMatchUiState,
    enabled: Boolean,
    actions: InsertMatchActions
) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        MatchLabel("${stringResource(Res.string.insert_match_formation)}:")
        state.formations.forEach { formation ->
            MatchChip(
                text = formation.shorten(),
                selected = state.formation == formation,
                enabled = enabled,
                onClick = { actions.updateField(Formation(formation)) }
            )
        }
    }
}

/**
 * Tapping an empty slot opens the picker for it; tapping a filled one frees it.
 */
@Composable
private fun MatchPitch(
    state: InsertMatchUiState,
    enabled: Boolean,
    actions: InsertMatchActions
) {
    /**
     * Keyed by formation: the field remembers which slots it has already shown, and a new
     * formation has to start that over.
     */
    key(state.formation) {
        GBFootballField(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(PITCH_ASPECT_RATIO)
                .clip(matchFieldShape)
                .border(width = 1.dp, color = lineUpColor.copy(alpha = 0.4f), shape = matchFieldShape),
            showAnimation = false,
            players = state.formation.positions.indices.associateWith { state.lineUp[it] },
            fieldImage = AppImages.footballField,
            playerPlaceholder = AppImages.facePlayer,
            formation = state.formation,
            onPlayerSelected = { _, index ->
                when {
                    !enabled -> Unit
                    index in state.lineUp -> actions.removePlayer(LineUp(index), index)
                    else -> actions.updateDialogState(InsertMatchDialog.Player(LineUp(index)))
                }
            }
        )
    }
}

/**
 * A list of people called up without a place on the pitch: the bench and the coaching staff.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MatchCallUp(
    title: String,
    subtitle: String?,
    players: List<GBPlayer>,
    target: InsertMatchPlayerTarget,
    enabled: Boolean,
    actions: InsertMatchActions
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(matchFieldShape)
            .border(width = 1.dp, color = matchCardBorder, shape = matchFieldShape)
            .padding(12.dp),
        verticalArrangement = spacedBy(10.dp)
    ) {
        Row(verticalAlignment = CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                GBText(
                    text = title,
                    style = GBTheme.typography.titleSmall.copy(fontWeight = Bold)
                )
                if (subtitle != null) {
                    GBText(
                        text = subtitle,
                        textColor = matchMutedText,
                        style = GBTheme.typography.bodySmall
                    )
                }
            }
            MatchAddButton(
                enabled = enabled,
                onClick = { actions.updateDialogState(InsertMatchDialog.Player(target)) }
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
                        onRemove = { actions.removePlayer(target, index) }
                    )
                }
            }
        }
    }
}

internal fun LineUpFormation.shorten(): String =
    when (this) {
        FOUR_THREE_THREE -> "4-3-3"
        FOUR_FOUR_TWO -> "4-4-2"
        FIVE_THREE_TWO -> "5-3-2"
    }
