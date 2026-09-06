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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.gaztelu_bira

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
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBAddButton
import com.sgale.gaztelubira.multiplatform.ui.UiDestination.FromGazteluBiraTab.InsertTeam
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.gaztelu_bira.ui.GBHomeInformationBox
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.gaztelu_bira.ui.GBRivals

@Composable
internal fun GazteluBiraViewUI(
    state: GazteluBiraUiState,
    actions: GazteluBiraActions
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GBHomeInformationBox(summary = state.season)
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        GBRivals(
            modifier = Modifier.weight(1f),
            teams = state.teams
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(top = 12.dp, end = 24.dp)
    ) {
        GBAddButton(
            show = state.isAdmin,
            modifier = Modifier.align(TopEnd),
            onButtonClicked = { actions.navigateTo(InsertTeam) }
        )
    }
}
