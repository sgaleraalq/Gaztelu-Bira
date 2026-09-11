/*
 * Designed and developed by 2026 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.multiplatform.ui.insert.team

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamImage
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamName
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamState.Companion.isLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamState.Companion.isInvalidInformation
import com.sgale.gaztelubira.multiplatform.ui.insert.team.ui.InsertTeamButton
import com.sgale.gaztelubira.multiplatform.ui.insert.team.ui.InsertTeamImage
import com.sgale.gaztelubira.multiplatform.ui.insert.team.ui.InsertTeamName

@Composable
internal fun InsertTeamViewUI(
    modifier: Modifier,
    state: InsertTeamUiState,
    actions: InsertTeamActions
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(16.dp)
    ) {
        InsertTeamName(
            teamName = state.teamName,
            loading = state.state.isLoading(),
            validInformation = !state.state.isInvalidInformation(),
            onTeamNameChanged =  { newName -> actions.updateField(TeamName(newName)) }
        )
        InsertTeamImage(
            img = state.teamImage,
            loading = state.state.isLoading(),
            onPickImage = actions.pickImage,
            updatePicture = { newImg -> actions.updateField(TeamImage(newImg)) }
        )
        Spacer(Modifier.weight(1f))
        InsertTeamButton(
            loading = state.state.isLoading(),
            onInsert = actions.insertTeam
        )
    }
}
