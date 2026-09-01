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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBDialog
import com.sgale.gaztelubira.core.designsystem.components.GBTeam
import com.sgale.gaztelubira.core.designsystem.components.GBTeamName
import com.sgale.gaztelubira.core.designsystem.components.GBText
import com.sgale.gaztelubira.core.designsystem.style.gBTypography
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.screens.R

@Composable
internal fun InsertMatchTeamsDialog(
    teams: List<TeamModel>,
    dismiss: () -> Unit,
    onTeamSelected: (TeamModel) -> Unit
) {
    GBDialog(
        dismiss = { dismiss() },
    ) { modifier ->
        LazyVerticalGrid(
            modifier = modifier.padding(16.dp),
            columns = GridCells.Fixed(4),
            verticalArrangement = spacedBy(16.dp),
            horizontalArrangement = spacedBy(16.dp),
        ) {
            item(span = { GridItemSpan(4) }){
                GBText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.select_a_team),
                    alignment = Center,
                    style = gBTypography().headlineSmall
                )
            }
            items(teams.size) { index ->
                val team = teams[index]
                TeamItem(
                    team = team,
                    onTeamSelected = {
                        onTeamSelected(team)
                        dismiss()
                    }
                )
            }
        }
    }
}

@Composable
internal fun TeamItem(
    team: TeamModel,
    onTeamSelected: () -> Unit
){
    Column(
        modifier = Modifier.clickable { onTeamSelected() },
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(8.dp)
    ) {
        GBTeam(Modifier.size(50.dp), team.logo)
        GBTeamName(name = team.name, style = gBTypography().bodySmall, maxLines = 2)
    }
}
