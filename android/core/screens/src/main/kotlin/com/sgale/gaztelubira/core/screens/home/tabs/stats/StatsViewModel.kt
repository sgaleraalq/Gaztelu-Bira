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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.player.Stat.Percentage
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.usecase.db.GetMatches
import com.sgale.gaztelubira.core.domain.usecase.db.GetPlayersStats
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.PlayerDisplayStats
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.StatsUiState
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.StatsUiState.Loading
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.StatsUiState.StatsLoaded
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getPlayerStats: GetPlayersStats,
    private val getMatches: GetMatches
) : ViewModel() {
    private val _uiState = MutableStateFlow<StatsUiState>(Loading)
    val uiState: StateFlow<StatsUiState> = _uiState
    private val _punctuation = MutableStateFlow(Punctuation())
    val punctuation: StateFlow<Punctuation> = _punctuation
    private val _selectedPlayer = MutableStateFlow<PlayerStatsModel?>(null)
    val selectedPlayer: StateFlow<PlayerStatsModel?> = _selectedPlayer
    private val playersStats = MutableStateFlow<List<PlayerStatsModel>>(emptyList())
    private val matches = MutableStateFlow<List<MatchModel>>(emptyList())
    private val selectedStat = MutableStateFlow(Percentage)

    val handler = StatsHandler(
        playersList = playersStats,
        selectedStat = selectedStat,
        matchesFlow = matches,
        scope = viewModelScope
    )

    val valueChanged = handler.valueChanged

    val players: StateFlow<List<PlayerDisplayStats>> = handler.statsDisplayed

    init {
        viewModelScope.launch {
            getPlayerStats()
                .flowOn(Dispatchers.IO)
                .collect { combined ->
                    playersStats.value = combined
                    _uiState.value = StatsLoaded(selectedStat.value)
                }
        }

        viewModelScope.launch {
            getMatches()
                .flowOn(Dispatchers.IO)
                .collect { list ->
                    matches.value = list
                }
        }
    }

    fun calculatePercentage(player: PlayerStatsModel): Double =
        handler.calculatePercentage(player, _punctuation.value)

    fun changeSelectedStat(stat: Stat) {
        selectedStat.value = stat
        _uiState.value = StatsLoaded(
            selectedStat = stat
        )
    }

    fun changePunctuation(punctuation: Punctuation) {
        _punctuation.value = punctuation
        handler.changePunctuation(punctuation)
    }

    fun selectPlayer(playerId: FirebaseId?) {
        if (playerId == null) {
            _selectedPlayer.value = null
        } else {
            _selectedPlayer.value = playersStats.value.find { it.id == playerId }
        }
    }
}
