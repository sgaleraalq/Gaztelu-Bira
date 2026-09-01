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

package com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBImage
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.components.SaverStatus.Team
import com.sgale.gaztelubira.core.designsystem.style.elevated_button_bg_not_selected
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.screens.R

@Composable
fun GBHomeTeams(
    modifier: Modifier,
    teams: List<TeamModel>
) {
    OtherTeamsText()
    LazyVerticalGrid(
        modifier = modifier,
        columns = Fixed(4),
        horizontalArrangement = spacedBy(8.dp),
        verticalArrangement = spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(teams) { team ->
            GBHomeTeam(team)
        }
    }
}

@Composable
fun OtherTeamsText() {
    GBText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        text = stringResource(R.string.other_teams),
        alignment = Center,
        style = gBTypography().titleLarge
    )
}

@Composable
fun GBHomeTeam(
    team: TeamModel
) {
    Column(
        modifier = Modifier.height(110.dp).background(
            color = elevated_button_bg_not_selected,
            shape = RoundedCornerShape(12.dp)
        ).padding(8.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(4.dp)
    ) {
        GBImage(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(50))
                .background(White)
                .border(
                    width = 1.dp,
                    color = White,
                    shape = RoundedCornerShape(50)
                ),
            imageModifier = Modifier.fillMaxSize(),
            image = team.logo,
            saverStatus = Team,
            contentScale = Fit
        )
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            GBText(
                modifier = Modifier.fillMaxWidth(),
                text = team.name,
                style = gBTypography().bodySmall,
                maxLines = 2,
                alignment = Center
            )
        }
    }
}
