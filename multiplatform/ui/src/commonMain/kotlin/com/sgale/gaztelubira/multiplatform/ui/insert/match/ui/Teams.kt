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
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBBasicTextField
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTeam
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTeamName
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_bg
import com.sgale.gaztelubira.multiplatform.designsystem.style.elevated_button_text_color
import com.sgale.gaztelubira.multiplatform.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.LEAGUE
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.AppImages
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchUiState
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchDialog.Teams
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.LocalGoals
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.ToggleSide
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.ToggleType
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchField.VisitorGoals
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.common_cup
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_add_team
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_journey
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_local
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_visitor
import org.jetbrains.compose.resources.stringResource

private val TEAM_SIZE = 50.dp

@Composable
internal fun InsertMatchTeams(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val pickRival = { actions.updateDialog(Teams) }

    Column(Modifier.padding(top = 16.dp)) {
        InsertMatchHeader(
            state = state,
            actions = actions
        )
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(8.dp)
        ) {
            MatchSide(
                modifier = Modifier.weight(1f),
                team = state.localTeam,
                goals = state.localGoals,
                isLocalSide = true,
                onGoalsChanged = { actions.updateField(LocalGoals(it)) },
                onTeamClicked = pickRival.takeIf { !state.isLocal }
            )
            GBText(
                modifier = Modifier.padding(bottom = 24.dp),
                text = "-"
            )
            MatchSide(
                modifier = Modifier.weight(1f),
                team = state.visitorTeam,
                goals = state.visitorGoals,
                isLocalSide = false,
                onGoalsChanged = { actions.updateField(VisitorGoals(it)) },
                onTeamClicked = pickRival.takeIf { state.isLocal }
            )
        }
    }
}

@Composable
private fun InsertMatchHeader(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        Column(Modifier.weight(1.5f)) {
            GBText(
                text = state.date,
                style = GBTheme.typography.bodySmall.copy(fontSize = 10.sp)
            )
            GBText(
                text = state.matchName(),
                style = GBTheme.typography.bodyLarge
            )
        }
        HeaderButton(
            modifier = Modifier.weight(1f),
            text = stringResource(state.type.label),
            onClick = { actions.updateField(ToggleType) }
        )
        HeaderButton(
            modifier = Modifier.weight(1f),
            text = stringResource(
                if (state.isLocal) Res.string.insert_match_local else Res.string.insert_match_visitor
            ),
            onClick = { actions.updateField(ToggleSide) },
            backgroundColor = if (state.isLocal) elevated_button_text_color else elevated_button_bg,
            textColor = if (state.isLocal) elevated_button_bg else elevated_button_text_color
        )
    }
}

@Composable
private fun InsertMatchUiState.matchName(): String =
    if (type == LEAGUE) {
        "${stringResource(Res.string.insert_match_journey)} $journey"
    } else {
        cupName.ifBlank { stringResource(Res.string.common_cup) }
    }

@Composable
private fun HeaderButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = elevated_button_bg,
    textColor: Color = elevated_button_text_color
) {
    GBElevatedButton(
        modifier = modifier.height(40.dp),
        text = text,
        onClick = onClick,
        textStyle = GBTheme.typography.bodySmall,
        backgroundColor = backgroundColor,
        textColor = textColor,
        padding = PaddingValues(12.dp)
    )
}

/**
 * @param onTeamClicked Null on the app's own side, which cannot be changed.
 */
@Composable
private fun MatchSide(
    modifier: Modifier,
    team: GBTeam?,
    goals: Int?,
    isLocalSide: Boolean,
    onGoalsChanged: (Int?) -> Unit,
    onTeamClicked: (() -> Unit)?
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {
        if (!isLocalSide) {
            Goals(goals = goals, onGoalsChanged = onGoalsChanged, showDoneButton = true)
        }
        SideTeam(
            modifier = Modifier.weight(1f),
            team = team,
            onTeamClicked = onTeamClicked
        )
        if (isLocalSide) {
            Goals(goals = goals, onGoalsChanged = onGoalsChanged)
        }
    }
}

@Composable
private fun SideTeam(
    modifier: Modifier,
    team: GBTeam?,
    onTeamClicked: (() -> Unit)?
) {
    Column(
        modifier = if (onTeamClicked != null) modifier.clickable { onTeamClicked() } else modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        if (team != null) {
            GBTeam(
                modifier = Modifier.size(TEAM_SIZE),
                image = team.logo,
                placeholder = AppImages.teamCrest
            )
            GBTeamName(Modifier.fillMaxWidth(), team.name)
        } else {
            Box(
                modifier = Modifier
                    .size(TEAM_SIZE)
                    .clip(RoundedCornerShape(50))
                    .background(gray_box_in_black_bg)
                    .border(width = 1.dp, color = White, shape = RoundedCornerShape(50))
            )
            GBTeamName(
                modifier = Modifier.fillMaxWidth(),
                name = stringResource(Res.string.insert_add_team)
            )
        }
    }
}

@Composable
private fun Goals(
    goals: Int?,
    onGoalsChanged: (Int?) -> Unit,
    showDoneButton: Boolean = false
) {
    Box(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .size(36.dp)
            .background(color = White, shape = RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        GBBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = goals?.toString().orEmpty(),
            onValueChanged = { onGoalsChanged(it.toIntOrNull()) },
            isNumeric = true,
            style = GBTheme.typography.bodyLarge.copy(
                color = Black,
                textAlign = Center
            ),
            showDoneButton = showDoneButton
        )
    }
}
