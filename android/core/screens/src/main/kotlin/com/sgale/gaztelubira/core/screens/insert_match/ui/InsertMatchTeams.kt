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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.designsystem.components.GBBasicTextField
import com.sgale.gaztelubira.core.designsystem.components.GBElevatedButton
import com.sgale.gaztelubira.core.designsystem.components.GBTeam
import com.sgale.gaztelubira.core.designsystem.components.GBTeamName
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.style.elevated_button_bg
import com.sgale.gaztelubira.core.designsystem.style.elevated_button_text_color
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.designsystem.style.gray_box_in_black_bg
import com.sgale.gaztelubira.core.domain.model.match.MatchType.League
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.team.TeamSide.Local
import com.sgale.gaztelubira.core.domain.model.team.TeamSide.Visitor
import com.sgale.gaztelubira.core.domain.utils.DATE_FORMAT
import com.sgale.gaztelubira.core.domain.utils.getDateFromLong
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.insert_match.InsertMatchTeamsViewModel
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchTeamsInformation
import androidx.compose.ui.res.stringResource

@Composable
internal fun InsertMatchTeams(
    appTeam: TeamModel?,
    selectedTeam: TeamModel?,
    onTeamClicked: () -> Unit,
    viewModel: InsertMatchTeamsViewModel
) {
    val journey = stringResource(R.string.journey)
    val cup = stringResource(R.string.cup)

    val matchInformation by viewModel.matchInformation.collectAsStateWithLifecycle()

    LaunchedEffect(appTeam) {
        viewModel.updateLocalTeam(appTeam, journey, cup)
    }

    LaunchedEffect(selectedTeam) {
        if (selectedTeam != null) {
            viewModel.updateSelectedTeam(selectedTeam)
        }
    }

    Column(Modifier.padding(top = 16.dp)) {
        InsertMatchInformationRow(
            matchInformation = matchInformation,
            changeMatchType = { viewModel.updateMatchType() },
            changeLocal = { viewModel.changeLocal() }
        )

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = spacedBy(8.dp)
        ) {
            InsertMatchTeamComponent(
                modifier = Modifier.weight(1f),
                team = matchInformation.local,
                goals = matchInformation.localGoals,
                local = true,
                onGoalsChanged = { viewModel.changeGoals(Local, it) },
                onTeamClicked = { if (!matchInformation.appTeamLocal) onTeamClicked() }
            )
            GBText(modifier = Modifier.padding(bottom = 24.dp), text = "-")
            InsertMatchTeamComponent(
                modifier = Modifier.weight(1f),
                team = matchInformation.visitor,
                goals = matchInformation.visitorGoals,
                local = false,
                onGoalsChanged = { viewModel.changeGoals(Visitor, it) },
                { if (matchInformation.appTeamLocal) onTeamClicked() }
            )
        }
    }
}

@Composable
internal fun InsertMatchInformationRow(
    matchInformation: InsertMatchTeamsInformation,
    changeMatchType: () -> Unit,
    changeLocal: () -> Unit
) {
    val matchTypeText = if (matchInformation.matchType == League) {
        stringResource(R.string.league)
    } else {
        stringResource(R.string.cup)
    }

    val matchSideText = if (matchInformation.appTeamLocal) {
        stringResource(R.string.local)
    } else {
        stringResource(R.string.visitor)
    }

    val isLocal = matchInformation.appTeamLocal

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        InsertMatchInformationTexts(
            modifier = Modifier.weight(1.5f),
            matchInformation = matchInformation
        )
        Spacer(Modifier.width(12.dp))
        InsertMatchButton(
            modifier = Modifier.weight(1f),
            text = matchTypeText,
            onButtonPressed = { changeMatchType() }
        )
        InsertMatchButton(
            modifier = Modifier.weight(1f),
            text = matchSideText,
            onButtonPressed = { changeLocal() },
            backgroundColor = if (!isLocal) {
                elevated_button_bg
            } else {
                elevated_button_text_color
            },
            textColor = if (!isLocal) {
                elevated_button_text_color
            } else {
                elevated_button_bg
            }
        )
    }
}

@Composable
internal fun InsertMatchInformationTexts(
    modifier: Modifier,
    matchInformation: InsertMatchTeamsInformation
) {
    Column(
        modifier = modifier
    ) {
        InsertMatchDate(date = matchInformation.date)
        InsertMatchName(matchInformation.matchName)
    }
}

@Composable
internal fun InsertMatchButton(
    modifier: Modifier,
    text: String,
    onButtonPressed: () -> Unit,
    backgroundColor: Color = elevated_button_bg,
    textColor: Color = elevated_button_text_color
) {
    GBElevatedButton(
        modifier = modifier.height(40.dp),
        text = text,
        onClick = onButtonPressed,
        textStyle = gBTypography().bodySmall,
        backgroundColor = backgroundColor,
        textColor = textColor,
        padding = PaddingValues(12.dp)
    )
}

@Composable
internal fun InsertMatchDate(
    modifier: Modifier = Modifier,
    date: Long
) {
    GBText(
        modifier = modifier,
        text = getDateFromLong(DATE_FORMAT, date),
        style = gBTypography().bodySmall.copy(
            fontSize = 10.sp
        )
    )
}

@Composable
internal fun InsertMatchName(name: String) {
    GBText(
        text = name,
        style = gBTypography().bodyLarge
    )
}

@Composable
internal fun InsertMatchTeamComponent(
    modifier: Modifier,
    team: TeamModel?,
    goals: Int,
    local: Boolean,
    onGoalsChanged: (Int?) -> Unit,
    onTeamClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {
        if (!local) {
            InsertMatchResult(
                result = goals,
                onResultChanged = { onGoalsChanged(it) },
                showDoneButton = true
            )
        }
        InsertPlayerTeam(Modifier.weight(1f), team) {
            onTeamClicked()
        }
        if (local) {
            InsertMatchResult(
                result = goals,
                onResultChanged = { onGoalsChanged(it) }
            )
        }
    }
}

@Composable
internal fun InsertPlayerTeam(
    modifier: Modifier,
    team: TeamModel?,
    onTeamClicked: () -> Unit
) {
    Column(
        modifier = modifier.clickable {
            onTeamClicked()
        },
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        if (team != null) {
            GBTeam(Modifier.size(50.dp), team.logo)
            GBTeamName(Modifier.fillMaxWidth(), team.name)
        } else {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(gray_box_in_black_bg)
                    .border(
                        width = 1.dp,
                        color = White,
                        shape = RoundedCornerShape(50)
                    )
            )
            GBTeamName(
                modifier = Modifier.fillMaxWidth(),
                name = stringResource(R.string.insert_team)
            )
        }
    }
}

@Composable
internal fun InsertMatchResult(
    result: Int,
    onResultChanged: (Int?) -> Unit,
    showDoneButton: Boolean = false
) {
    Box(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .size(36.dp)
            .background(
                color = White,
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        GBBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = if (result == -1) "" else result.toString(),
            onValueChanged = { new ->
                onResultChanged(new.toIntOrNull())
            },
            isNumeric = true,
            style = gBTypography().bodyLarge.copy(
                color = Black,
                textAlign = Center
            ),
            showDoneButton = showDoneButton
        )
    }
}
