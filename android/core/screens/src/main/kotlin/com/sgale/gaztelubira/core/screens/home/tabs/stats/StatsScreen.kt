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

package com.sgale.gaztelubira.core.screens.home.tabs.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.screens.navigation.NavigationState

@Composable
fun StatsScreen(
    state: NavigationState,
    viewModel: StatsViewModel = hiltViewModel<StatsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val players by viewModel.players.collectAsStateWithLifecycle()
    val punctuation by viewModel.punctuation.collectAsStateWithLifecycle()
    val selectedPlayer by viewModel.selectedPlayer.collectAsStateWithLifecycle()
    val valueChanged by viewModel.valueChanged.collectAsStateWithLifecycle()

    StatsScreenUI(
        state = uiState,
        punctuation = punctuation,
        players = players,
        selectedPlayer = selectedPlayer,
        valueChanged = valueChanged,
        calculatePercentage = { viewModel.calculatePercentage(it) },
        changeSelectedStat = { viewModel.changeSelectedStat(it) },
        changePunctuation = { viewModel.changePunctuation(it) },
        selectPlayer = { viewModel.selectPlayer(it) }
    )
}
