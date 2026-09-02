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

package com.sgale.gaztelubira.core.screens.home.tabs.matches.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.End
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.match.MatchType
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.utils.toDate
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText

@Composable
fun MatchesList(
    matches: List<MatchModel>,
    matchResult: (MatchModel) -> MatchResult,
    onMatchClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = spacedBy(12.dp)
    ) {
        items(
            items = matches.sortedByDescending { it.date },
            key = { match -> match.id }
        ) { match ->
            val result = matchResult(match)
            GBMatch(
                matchResult = result,
                match = match,
                onMatchClicked = { id -> onMatchClicked(id) }
            )
        }
    }
}

@Composable
fun GBMatch(
    matchResult: MatchResult,
    match: MatchModel,
    onMatchClicked: (String) -> Unit,
) {
    Card(
        modifier = Modifier.clickable{
            onMatchClicked(match.id)
        },
        colors = CardDefaults.cardColors(
            containerColor = matchResult.transColor
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            MatchHeader(match.matchName, match.matchType)
            Spacer(Modifier.height(12.dp))
            MatchResult(match)
            MatchInformation(match.date.toDate())
        }
    }
}

@Composable
fun MatchHeader(
    matchName: String,
    matchType: MatchType
) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalAlignment = CenterVertically
    ) {
        MatchName(Modifier.weight(1f), matchName)
        MatchType(matchType)
    }
}

@Composable
fun MatchName(modifier: Modifier = Modifier, name: String) {
    GBText(
        modifier = modifier,
        text = name,
        alignment = Start,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun MatchType(matchType: MatchType) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        GBText(
            text = stringResource(matchType.type).lowercase().capitalize(Locale.current),
            style = MaterialTheme.typography.bodySmall
        )
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(matchType.icon),
            contentDescription = null,
            tint = Unspecified
        )
    }
}

@Composable
fun MatchResult(match: MatchModel) {
    val teamLogoHeight = 48.dp
    Row(
        verticalAlignment = CenterVertically
    ) {
        MatchTeam(
            modifier = Modifier.weight(1f),
            logoHeight = teamLogoHeight,
            team = match.localTeam,
            goals = match.localGoals.toString()
        )
        GBText(
            modifier = Modifier.padding(horizontal = 16.dp).height(teamLogoHeight),
            text = "-",
            alignment = Center
        )
        MatchTeam(
            modifier = Modifier.weight(1f),
            logoHeight = teamLogoHeight,
            team = match.visitorTeam,
            goals = match.visitorGoals.toString(),
            isLocal = false
        )
    }
}

@Composable
fun MatchTeam(
    modifier: Modifier,
    logoHeight: Dp,
    team: TeamModel,
    goals: String,
    isLocal: Boolean = true
) {
    /**
     * If team is local, goals should be displayed after team logo
     */
    Row(modifier) {
        if (!isLocal) {
            GBVisitorGoals(
                modifier = Modifier.weight(1f).height(logoHeight),
                goals = goals
            )
        }
        GBMatchResultTeam(
            image = team.logo ?: "",
            teamName = team.name,
            logoHeight = logoHeight
        )
        if (isLocal) {
            GBLocalGoals(
                modifier = Modifier.weight(1f).height(logoHeight),
                goals = goals
            )
        }
    }
}

@Composable
fun GBLocalGoals(
    modifier: Modifier,
    goals: String
){
    Box(modifier, contentAlignment = Alignment.Center) {
        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = goals,
            alignment = End,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun GBVisitorGoals(
    modifier: Modifier,
    goals: String
){
    Box(modifier, contentAlignment = Alignment.Center){
        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = goals,
            alignment = Start,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun GBMatchResultTeam(
    image: String,
    teamName: String,
    logoHeight: Dp
) {
    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        GBTeam(Modifier.size(logoHeight), image)
        GBTeamName(teamName)
    }
}

@Composable
fun GBTeamName(teamName: String) {
    GBText(
        modifier = Modifier.fillMaxWidth(),
        text = teamName,
        alignment = Center,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun MatchInformation(date: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(4.dp)
    ) {
        MatchDate(date)
    }
}

@Composable
fun MatchDate(
    date: String = "01/01/2025"
) {
    GBText(
        text = date,
        alignment = Start,
        style = MaterialTheme.typography.bodySmall.copy(
            fontStyle = Italic
        )
    )
}
