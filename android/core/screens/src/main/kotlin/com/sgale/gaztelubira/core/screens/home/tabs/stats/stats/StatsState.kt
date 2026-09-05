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

package com.sgale.gaztelubira.core.screens.home.tabs.stats.stats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.PlayerDisplayStats
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.StatsUiState
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.StatsUiState.StatsLoaded

@Composable
fun StatsState(
    state: StatsUiState,
    modifier: (Float) -> Modifier,
    players: List<PlayerDisplayStats>,
    selectPlayer: (FirebaseId?) -> Unit,
    showSettings: () -> Unit
) {
    state as StatsLoaded

    StatsTitle(
        onSettingsClicked = { showSettings() }
    )
    StatsLeaderboard(
        selectedStat = state.selectedStat,
        first = players.firstOrNull(),
        second = players.getOrNull(1),
        third = players.getOrNull(2),
        selectPlayer = { selectPlayer(it) }
    )
    SelectedStatTitle(state.selectedStat)
    StatsClassification(
        modifier = modifier(0.6f),
        players = players,
        selectedStat = state.selectedStat,
        selectPlayer = { selectPlayer(it) }
    )
}
