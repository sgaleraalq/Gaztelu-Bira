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

import androidx.compose.runtime.Composable
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal.DismissPlayer
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsSettings.Hidden
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsState.Loaded
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsState.Loading
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.StatsLoaded
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.StatsLoading
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.dialogs.PlayerCard
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.ui.dialogs.Settings

@Composable
internal fun StatsViewUI(
    state: StatsUiState,
    actions: StatsActions
) {
    when (state.state) {
        Loading -> StatsLoading()
        Loaded -> StatsLoaded(state, actions)
    }

    PlayerCard(
        player = state.selectedPlayer,
        onDismiss = { actions.onPlayerAction(DismissPlayer) }
    )

    Settings(
        show = state.settings != Hidden,
        settings = state.settings,
        selectedStat = state.selectedStat,
        punctuation = state.punctuation,
        actions = actions
    )
}
