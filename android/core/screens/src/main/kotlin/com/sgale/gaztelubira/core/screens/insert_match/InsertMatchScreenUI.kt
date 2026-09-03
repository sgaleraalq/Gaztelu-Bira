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

package com.sgale.gaztelubira.core.screens.insert_match

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchState
import com.sgale.gaztelubira.core.screens.insert_match.data.InsertMatchTeamsInformation
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchButtons
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchTeams
import com.sgale.gaztelubira.core.screens.insert_match.ui.InsertMatchTeamsDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBInsertButton

@Composable
internal fun InsertMatchScreenUI(
    modifier: Modifier,
    team: TeamModel?,
    teams: List<TeamModel>,
    loading: Boolean,
    changeState: (InsertMatchState) -> Unit,
    viewModel: InsertMatchTeamsViewModel = hiltViewModel(),
    insertMatch: (InsertMatchTeamsInformation) -> Unit
) {
    var showTeams by remember { mutableStateOf(false) }
    var selectedTeam by remember { mutableStateOf<TeamModel?>(null) }
    val matchInformation by viewModel.matchInformation.collectAsStateWithLifecycle()

    Column (
        modifier = modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(12.dp)
    ){
        InsertMatchTeams(
            appTeam = team,
            selectedTeam = selectedTeam,
            onTeamClicked = { showTeams = true },
            viewModel = viewModel
        )
        InsertMatchButtons(
            modifier = Modifier.weight(1f),
            changeState = { newState -> changeState(newState) }
        )
        GBInsertButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.insert_match),
            enabled = true,
            loading = loading,
            onInsert = { insertMatch(matchInformation) }
        )
    }

    if (showTeams) {
        InsertMatchTeamsDialog(
            teams = teams,
            dismiss = { showTeams = false },
            onTeamSelected = { selectedTeam = it }
        )
    }
}
