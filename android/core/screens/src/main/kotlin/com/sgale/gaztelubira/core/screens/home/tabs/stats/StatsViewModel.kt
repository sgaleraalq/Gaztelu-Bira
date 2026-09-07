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

package com.sgale.gaztelubira.core.screens.home.tabs.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.stats.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.stats.StatsMapper.toDetail
import com.sgale.gaztelubira.core.domain.model.stats.StatsMapper.toGBPlayerStat
import com.sgale.gaztelubira.core.domain.model.stats.StatsMapper.toStat
import com.sgale.gaztelubira.core.domain.usecase.db.GetMatches
import com.sgale.gaztelubira.core.domain.usecase.db.GetPlayersStats
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.PERCENTAGE
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsUiState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsPlayerModal.*
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsSettings
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsState.Companion.computing
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.state.GBStatsState.Loaded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class StatsViewModel @Inject constructor(
    private val getPlayerStats: GetPlayersStats,
    private val getMatches: GetMatches
) : ViewModel() {
    private val _state = MutableStateFlow(StatsUiState())
    internal val state: StateFlow<StatsUiState> = _state.asStateFlow()

    private val playersStats = MutableStateFlow<List<PlayerStatsModel>?>(null)
    private val matches = MutableStateFlow<List<MatchModel>?>(null)
    private val selectedStat = MutableStateFlow(PERCENTAGE.toStat())

    private val handler = StatsHandler(
        playersList = playersStats.filterNotNull(),
        selectedStat = selectedStat,
        matchesFlow = matches.filterNotNull(),
        scope = viewModelScope
    )

    init {
        viewModelScope.launch {
            getPlayerStats()
                .flowOn(Dispatchers.IO)
                .collect { playersStats.value = it }
        }

        viewModelScope.launch {
            getMatches()
                .flowOn(Dispatchers.IO)
                .collect { matches.value = it }
        }

        viewModelScope.launch {
            handler.statsDisplayed.filterNotNull().collect { ranking ->
                _state.update { state ->
                    state.copy(
                        state = Loaded,
                        players = ranking.map { it.toGBPlayerStat(state.selectedStat) }
                    )
                }
            }
        }

        viewModelScope.launch {
            handler.valueChanged.collect { recomputing ->
                if (!recomputing) return@collect
                _state.update { state ->
                    state.copy(state = computing(state.players.isEmpty()))
                }
            }
        }
    }

    internal fun onStatSelected(stat: GBStat) {
        selectedStat.value = stat.toStat()
        _state.update { it.copy(selectedStat = stat) }
    }

    internal fun onPunctuationDraftChanged(punctuation: GBPunctuation) {
        _state.update { state ->
            state.copy(settings = GBStatsSettings.ChangePunctuation(punctuation))
        }
    }

    /**
     * Only now does the leaderboard get rescored: the sliders were editing a draft.
     */
    internal fun onPunctuationConfirmed(punctuation: GBPunctuation) {
        _state.update { it.copy(punctuation = punctuation) }
        handler.changePunctuation(punctuation)
    }

    internal fun onSettingsChanged(settings: GBStatsSettings) {
        _state.update { it.copy(settings = settings) }
    }

    internal fun onPlayerAction(action: GBStatsPlayerModal) {
        when (action) {
            DismissPlayer -> onPlayerDismissed()
            is ShowPlayer -> onPlayerSelected(action.id)
        }
    }

    private fun onPlayerSelected(playerId: String) {
        val player = playersStats.value?.find { it.id == playerId } ?: return
        val percentage = handler.calculatePercentage(player, _state.value.punctuation)
        _state.update { it.copy(selectedPlayer = player.toDetail(percentage)) }
    }

    private fun onPlayerDismissed() {
        _state.update { it.copy(selectedPlayer = null) }
    }
}
