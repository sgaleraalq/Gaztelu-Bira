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

package com.sgale.gaztelubira.core.screens.match_detail.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBBackButton
import com.sgale.gaztelubira.core.designsystem.components.GBTeam
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.multiplatform.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.domain.model.team.TeamModel

@Composable
fun MatchDetailHeader(
    localTeam: TeamModel?,
    localGoals: Int,
    visitorTeam: TeamModel?,
    visitorGoals: Int,
    onBackPressed: () -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
        GBBackButton { onBackPressed() }
        GBMatchDetailResult(
            modifier = Modifier.weight(1f),
            localTeam = localTeam,
            localGoals = localGoals,
            visitorTeam = visitorTeam,
            visitorGoals = visitorGoals
        )
        GBBackButton(isVisible = false) { }
    }
}

@Composable
fun GBMatchDetailResult(
    modifier: Modifier,
    localTeam: TeamModel?,
    localGoals: Int,
    visitorTeam: TeamModel?,
    visitorGoals: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {
        GBTeamDetailResult(Modifier.weight(1f), localTeam, localGoals)
        GBText(":", Modifier.padding(horizontal = 8.dp))
        GBTeamDetailResult(Modifier.weight(1f), visitorTeam, visitorGoals, false)
    }

}

@Composable
fun GBTeamDetailResult(
    modifier: Modifier,
    teamModel: TeamModel?,
    goals: Int,
    isLocal: Boolean = true
) {
    Row(modifier, verticalAlignment = CenterVertically) {
        if (!isLocal) {
            GBText(
                text = goals.toString(),
                style = gBTypography().headlineMedium
            )
        }
        Spacer(Modifier.weight(1f))
        GBTeam(Modifier.size(36.dp), teamModel?.logo)
        Spacer(Modifier.weight(1f))
        if (isLocal) {
            GBText(
                text = goals.toString(),
                style = gBTypography().headlineMedium
            )
        }
    }
}
