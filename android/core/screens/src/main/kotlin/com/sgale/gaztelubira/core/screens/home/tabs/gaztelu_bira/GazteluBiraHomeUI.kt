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

package com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.designsystem.components.GBAddButton
import com.sgale.gaztelubira.core.domain.auth.UserSession
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.ui.GBHomeInformationBox
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.ui.GBHomeTeams

@Composable
fun GazteluBiraHomeUI(
    user: UserSession?,
    teams: List<TeamModel>,
    gbInformation: GBInformation?,
    navigateToInsertTeam: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        GBHomeInformationBox(gbInformation)
        HorizontalDivider(Modifier.fillMaxWidth().padding(top = 8.dp), thickness = 1.dp)
        GBHomeTeams(Modifier.weight(1f), teams)
    }

    if (user?.isAdmin() == true) {
        Box(Modifier.fillMaxSize().padding(top = 12.dp, end = 24.dp)) {
            GBAddButton(Modifier.align(TopEnd)) { navigateToInsertTeam() }
        }
    }
}
