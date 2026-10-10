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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBTeam
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.model.GBMatch
import com.sgale.gaztelubira.multiplatform.model.GBMatchTeam
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.img_gaztelu_bira
@Composable
internal fun MatchScore(
    match: GBMatch
) {
    val teamLogoHeight = 48.dp
    Row(
        verticalAlignment = CenterVertically
    ) {
        MatchTeam(
            modifier = Modifier.weight(1f),
            logoHeight = teamLogoHeight,
            team = match.localTeam,
        )
        GBText(
            modifier = Modifier.padding(horizontal = 16.dp).height(teamLogoHeight),
            text = "-",
            alignment = TextAlign.Center
        )
        MatchTeam(
            modifier = Modifier.weight(1f),
            logoHeight = teamLogoHeight,
            team = match.visitorTeam,
            isLocal = false
        )
    }
}

@Composable
private fun MatchTeam(
    modifier: Modifier,
    logoHeight: Dp,
    team: GBMatchTeam,
    isLocal: Boolean = true
) {
    Row(
        modifier = modifier
    ) {
        if (!isLocal) {
            MatchGoals(
                modifier = Modifier.weight(1f).height(logoHeight),
                goals = team.goals.toString(),
                alignment = Start
            )
        }
        MatchTeamBadge(
            isGaztelu = team.isGaztelu,
            image = team.logo,
            teamName = team.name,
            logoHeight = logoHeight
        )
        if (isLocal) {
            MatchGoals(
                modifier = Modifier.weight(1f).height(logoHeight),
                goals = team.goals.toString(),
                alignment = TextAlign.End
            )
        }
    }
}

@Composable
private fun MatchGoals(
    modifier: Modifier,
    goals: String,
    alignment: TextAlign
) {
    Box(
        modifier = modifier,
        contentAlignment = Center
    ) {
        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = goals,
            alignment = alignment,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
private fun MatchTeamBadge(
    isGaztelu: Boolean,
    image: String,
    teamName: String,
    logoHeight: Dp
) {
    val imageModifier = Modifier.size(logoHeight)

    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        if (isGaztelu) {
            GBTeam(
                modifier = imageModifier,
                image = Res.drawable.img_gaztelu_bira
            )
        } else {
            GBTeam(
                modifier = imageModifier,
                image = image
            )
        }

        GBText(
            modifier = Modifier.fillMaxWidth(),
            text = teamName,
            alignment = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
