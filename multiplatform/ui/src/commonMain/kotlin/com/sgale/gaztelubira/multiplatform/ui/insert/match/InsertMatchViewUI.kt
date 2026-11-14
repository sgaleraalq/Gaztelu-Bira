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

package com.sgale.gaztelubira.multiplatform.ui.insert.match

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.GBTheme
import com.sgale.gaztelubira.multiplatform.designsystem.style.primaryRed
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.CUP
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.LEAGUE
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.Back
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.MatchCompetition
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.MatchInformation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.MatchLineUp
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.MatchScore
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.MatchSide
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.MatchStats
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.matchBackground
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.matchCardBorder
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.matchCardColor
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.matchMutedText
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.ic_arrow_left
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_journey
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_new_match
import com.sgale.gaztelubira.multiplatform.ui.resources.insert_match_save
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * The header and the save button stay put; only the form between them scrolls.
 */
@Composable
internal fun InsertMatchViewUI(
    modifier: Modifier,
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(matchBackground)
            .imePadding()
    ) {
        InsertMatchHeader(state, actions)
        HorizontalDivider(color = matchCardBorder)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = spacedBy(12.dp)
        ) {
            MatchCompetition(state, actions)
            MatchSide(state, actions)
            MatchScore(state, actions)
            MatchInformation(state, actions)
            MatchLineUp(state, actions)
            MatchStats(state, actions)
        }
        HorizontalDivider(color = matchCardBorder)
        InsertMatchButton(state, actions)
    }
}

@Composable
private fun InsertMatchHeader(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(matchCardColor)
                .border(width = 1.dp, color = matchCardBorder, shape = CircleShape)
                .clickable(enabled = state.state.isNotLoading()) { actions.navigateTo(Back) },
            contentAlignment = Center
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(Res.drawable.ic_arrow_left),
                contentDescription = null,
                tint = White
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            GBText(
                text = stringResource(Res.string.insert_match_new_match),
                style = GBTheme.typography.titleLarge.copy(fontWeight = Bold)
            )
            GBText(
                text = state.subtitle(),
                textColor = matchMutedText,
                style = GBTheme.typography.bodySmall,
                overflow = Ellipsis
            )
        }
    }
}

/**
 * "Jornada 20 • 2025/26" in the league; a cup has no matchday, so its own name stands there.
 */
@Composable
private fun InsertMatchUiState.subtitle(): String {
    val competition = when (type) {
        LEAGUE -> "${stringResource(Res.string.insert_match_journey)} $journey"
        CUP -> cupName.ifBlank { stringResource(type.label) }
    }
    return listOf(competition, seasonName).filter { it.isNotBlank() }.joinToString(" • ")
}

/**
 * A missing field is not a disabled button: the button stays live and says what is missing, the
 * same way the other insert screens do.
 */
@Composable
private fun InsertMatchButton(
    state: InsertMatchUiState,
    actions: InsertMatchActions
) {
    val invalidMessage = state.handler.invalidReason?.let { stringResource(it.reason) }
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(54.dp)
            .clip(shape)
            .background(primaryRed)
            .clickable(enabled = state.state.isNotLoading()) {
                if (invalidMessage != null) {
                    actions.onMissingField(invalidMessage)
                } else {
                    actions.insertMatch()
                }
            },
        contentAlignment = Center
    ) {
        if (state.state.isLoading()) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = White,
                strokeWidth = 2.dp
            )
        } else {
            GBText(
                text = stringResource(Res.string.insert_match_save),
                style = GBTheme.typography.titleMedium.copy(fontWeight = Bold)
            )
        }
    }
}
