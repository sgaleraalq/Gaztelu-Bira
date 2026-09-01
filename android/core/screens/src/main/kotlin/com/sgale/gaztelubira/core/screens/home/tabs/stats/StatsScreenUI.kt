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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.player.Stat.Percentage
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.utils.formatDecimal
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.PlayerDisplayStats
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.StatsUiState
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.StatsUiState.Loading
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.StatsUiState.StatsLoaded
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.GBSelectedPlayerDialog
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.StatsSettingsDialog
import com.sgale.gaztelubira.core.screens.home.tabs.stats.stats.StatsLoading
import com.sgale.gaztelubira.core.screens.home.tabs.stats.stats.StatsState

@Composable
fun StatsScreenUI(
    state: StatsUiState,
    punctuation: Punctuation,
    players: List<PlayerDisplayStats>,
    selectedPlayer: PlayerStatsModel?,
    valueChanged: Boolean,
    calculatePercentage: (PlayerStatsModel) -> Double,
    changeSelectedStat: (Stat) -> Unit,
    changePunctuation: (Punctuation) -> Unit,
    selectPlayer: (FirebaseId?) -> Unit
) {
    var showSettings by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        when (state) {
            Loading -> StatsLoading()
            is StatsLoaded -> {
                StatsState(
                    state = state,
                    modifier = { Modifier.weight(it) },
                    players = players,
                    selectPlayer = { selectPlayer(it) },
                    showSettings = { showSettings = true }
                )
            }
        }
    }

    GBSelectedPlayerDialog(
        player = selectedPlayer,
        calculatePercentage = { calculatePercentage(it) },
        onDismiss = { selectPlayer(null) }
    )


    if (showSettings && state is StatsLoaded) {
        StatsSettingsDialog(
            selectedStat = state.selectedStat,
            punctuation = punctuation,
            dismiss = { showSettings = false },
            changeSelectedStat = { changeSelectedStat(it) },
            changePunctuation = { changePunctuation(it) }
        )
    }

    if (valueChanged) {
        StatsLoading()
    }
}

internal fun displayStat(
    stat: Stat,
    value: Double?
): String {
    return try {
        when (stat) {
            Percentage -> formatDecimal(value)
            else -> value?.toInt().toString()
        }
    } catch (e: Exception) {
        println("Error displaying stat ${e.message}")
        ""
    }
}
