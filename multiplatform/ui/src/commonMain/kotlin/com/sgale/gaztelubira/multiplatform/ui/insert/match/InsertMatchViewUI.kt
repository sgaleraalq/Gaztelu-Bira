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

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Formation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Information
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Main
import com.sgale.gaztelubira.multiplatform.ui.insert.match.state.InsertMatchSection.Stats
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.InsertMatchButton
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.InsertMatchFormation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.InsertMatchInformation
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.InsertMatchSectionCard
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.InsertMatchSections
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.InsertMatchStats
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.InsertMatchTeams
import com.sgale.gaztelubira.multiplatform.ui.insert.match.ui.dialogs.InsertMatchDialogs

/**
 * The main page holds the teams and the score; information, formation and stats each open on
 * their own, with their card on top to go back.
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
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(12.dp)
    ) {
        when (val section = state.section) {
            Main -> {
                InsertMatchTeams(
                    state = state,
                    actions = actions
                )
                InsertMatchSections(
                    modifier = Modifier.weight(1f),
                    onSectionSelected = actions.changeSection
                )
                InsertMatchButton(
                    state = state,
                    actions = actions
                )
            }

            Information, Formation, Stats -> {
                InsertMatchSectionCard(
                    section = section,
                    onClick = { actions.changeSection(Main) }
                )
                when (section) {
                    Information -> InsertMatchInformation(Modifier.weight(1f), state, actions)
                    Formation -> InsertMatchFormation(Modifier.weight(1f), state, actions)
                    else -> InsertMatchStats(Modifier.weight(1f), state, actions)
                }
            }
        }
    }

    InsertMatchDialogs(
        state = state,
        actions = actions
    )
}
