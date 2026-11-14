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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTeam
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.model.GBMatchType
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.CUP
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.CupName
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.CupRound
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Local
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.RivalGoals
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.TeamGoals
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.Type
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_button_add
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_cup_name
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_cup_round
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_local
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_rival
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_score
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_tap_to_assign
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_visitor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Which competition the match belongs to. A cup has no matchday, so it asks for its name and
 * round instead.
 */
@Composable
internal fun MatchCompetition(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val enabled = state.state.isNotLoading()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(matchFieldShape)
            .background(matchCardColor)
            .border(width = 1.dp, color = matchCardBorder, shape = matchFieldShape)
            .padding(4.dp),
        horizontalArrangement = spacedBy(4.dp)
    ) {
        GBMatchType.entries.forEach { type ->
            MatchChip(
                modifier = Modifier.weight(1f),
                text = stringResource(type.label),
                selected = state.type == type,
                enabled = enabled,
                onClick = { actions.updateField(Type(type)) }
            )
        }
    }

    if (state.type == CUP) {
        Row(horizontalArrangement = spacedBy(12.dp)) {
            MatchTextField(
                modifier = Modifier.weight(1f),
                value = state.cupName,
                onValueChanged = { actions.updateField(CupName(it)) },
                hint = stringResource(Res.string.insert_match_cup_name),
                enabled = enabled
            )
            MatchTextField(
                modifier = Modifier.weight(1f),
                value = state.cupRound,
                onValueChanged = { actions.updateField(CupRound(it)) },
                hint = stringResource(Res.string.insert_match_cup_round),
                enabled = enabled
            )
        }
    }
}

/**
 * Whether Gaztelu Bira plays at home or away.
 */
@Composable
internal fun MatchSide(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val enabled = state.state.isNotLoading()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(8.dp)
    ) {
        MatchChip(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.insert_match_local),
            selected = state.isLocal,
            enabled = enabled,
            onClick = { actions.updateField(Local(true)) }
        )
        MatchChip(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.insert_match_visitor),
            selected = !state.isLocal,
            enabled = enabled,
            onClick = { actions.updateField(Local(false)) }
        )
    }
}

/**
 * Both teams and the score between them. The home side is always on the left, so switching sides
 * swaps the two halves — crest, name and goals together.
 */
@Composable
internal fun MatchScore(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val enabled = state.state.isNotLoading()

    val team: @Composable RowScope.() -> Unit = {
        MatchTeam(
            modifier = Modifier.weight(1f),
            team = state.team,
            fallbackName = "",
            hint = null,
            onClick = null
        )
    }
    val rival: @Composable RowScope.() -> Unit = {
        MatchTeam(
            modifier = Modifier.weight(1f),
            team = state.rival,
            fallbackName = stringResource(Res.string.insert_match_rival),
            hint = stringResource(Res.string.insert_match_tap_to_assign),
            onClick = { actions.updateDialogState(InsertMatchDialog.Rival) }.takeIf { enabled }
        )
    }
    val teamGoals: @Composable () -> Unit = {
        MatchGoals(
            goals = state.teamGoals,
            enabled = enabled,
            onGoalsChanged = { actions.updateField(TeamGoals(it)) }
        )
    }
    val rivalGoals: @Composable () -> Unit = {
        MatchGoals(
            goals = state.rivalGoals,
            enabled = enabled,
            onGoalsChanged = { actions.updateField(RivalGoals(it)) }
        )
    }

    MatchCard {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 20.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            if (state.isLocal) team() else rival()
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = spacedBy(8.dp)
                ) {
                    if (state.isLocal) teamGoals() else rivalGoals()
                    GBText(
                        text = ":",
                        textColor = matchMutedText,
                        style = GBTheme.typography.titleLarge
                    )
                    if (state.isLocal) rivalGoals() else teamGoals()
                }
                GBText(
                    text = stringResource(Res.string.insert_match_score).uppercase(),
                    textColor = matchMutedText,
                    style = GBTheme.typography.labelSmall.copy(fontWeight = Bold)
                )
            }
            if (state.isLocal) rival() else team()
        }
    }
}

/**
 * @param onClick Null for the team that cannot be changed.
 */
@Composable
private fun MatchTeam(
    modifier: Modifier,
    team: GBTeam?,
    fallbackName: String,
    hint: String?,
    onClick: (() -> Unit)?
) {
    Column(
        modifier = modifier
            .clip(matchFieldShape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(4.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(6.dp)
    ) {
        if (team != null) {
            GBTeam(
                modifier = Modifier.size(64.dp),
                image = team.logo,
                placeholder = AppImages.teamCrest
            )
        } else {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(matchFieldColor)
                    .border(width = 1.dp, color = matchCardBorder, shape = CircleShape),
                contentAlignment = Center
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(Res.drawable.ic_button_add),
                    contentDescription = null,
                    tint = matchMutedText
                )
            }
        }
        GBText(
            text = team?.name ?: fallbackName,
            alignment = TextAlign.Center,
            style = GBTheme.typography.labelLarge.copy(fontWeight = Bold),
            maxLines = 2,
            overflow = Ellipsis
        )
        if (team == null && hint != null) {
            GBText(
                text = hint,
                textColor = matchMutedText,
                alignment = TextAlign.Center,
                style = GBTheme.typography.labelSmall,
                maxLines = 2,
                overflow = Ellipsis
            )
        }
    }
}

@Composable
private fun MatchGoals(
    goals: String,
    enabled: Boolean,
    onGoalsChanged: (String) -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    BasicTextField(
        modifier = Modifier
            .width(52.dp)
            .clip(shape)
            .background(White)
            .padding(vertical = 14.dp),
        value = goals,
        onValueChange = onGoalsChanged,
        enabled = enabled,
        singleLine = true,
        textStyle = GBTheme.typography.headlineSmall.copy(
            color = Black,
            fontWeight = Bold,
            textAlign = TextAlign.Center
        ),
        cursorBrush = SolidColor(primaryRed),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Center) {
                if (goals.isEmpty()) {
                    GBText(
                        text = "-",
                        textColor = matchMutedText,
                        style = GBTheme.typography.headlineSmall.copy(fontWeight = Bold)
                    )
                }
                innerTextField()
            }
        }
    )
}
