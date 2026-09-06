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

package com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.GBStatsSettings.Hidden
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.GBStatsSettings.Menu
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.dialogs.StatsPlayerDialog
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.dialogs.StatsSettingsDialog
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.SelectedStatTitle
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.StatsClassification
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.StatsLeaderboard
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.StatsLoading
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.StatsTitle

@Composable
internal fun StatsViewUI(
    state: StatsUiState,
    actions: StatsActions
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp)
    ) {
        if (state.isLoading) {
            StatsLoading()
        } else {
            StatsTitle(onSettingsClicked = { actions.onSettingsChanged(Menu) })
            StatsLeaderboard(
                first = state.players.getOrNull(0),
                second = state.players.getOrNull(1),
                third = state.players.getOrNull(2),
                onPlayerSelected = actions.onPlayerSelected
            )
            SelectedStatTitle(state.selectedStat)
            StatsClassification(
                modifier = Modifier.weight(0.6f),
                players = state.players,
                selectPlayer = actions.onPlayerSelected
            )
        }
    }

    StatsPlayerDialog(
        player = state.selectedPlayer,
        onDismiss = actions.onPlayerDismissed
    )

    if (state.settings != Hidden) {
        StatsSettingsDialog(
            settings = state.settings,
            selectedStat = state.selectedStat,
            punctuation = state.punctuation,
            actions = actions
        )
    }
}
