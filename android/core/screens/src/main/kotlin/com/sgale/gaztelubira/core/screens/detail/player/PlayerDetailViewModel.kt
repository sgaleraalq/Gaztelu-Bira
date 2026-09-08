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

package com.sgale.gaztelubira.core.screens.detail.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerMapper.toGBPlayer
import com.sgale.gaztelubira.core.domain.model.stats.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.domain.usecase.db.FetchMatches
import com.sgale.gaztelubira.core.domain.usecase.firestore.FetchPlayer
import com.sgale.gaztelubira.core.domain.usecase.firestore.FetchPlayerStats
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailUiState.PlayerWinRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class PlayerDetailViewModel @Inject constructor(
    private val fetchPlayerInformation: FetchPlayer,
    private val fetchPlayerStats: FetchPlayerStats,
    private val fetchMatches: FetchMatches
) : ViewModel() {
    private val _state = MutableStateFlow(PlayerDetailUiState())
    internal val state: StateFlow<PlayerDetailUiState> = _state

    private fun calculateMatchesStats(
        appTeam: TeamModel?,
        matches: List<MatchModel>,
        playerStats: PlayerStatsModel
    ) {
        if (appTeam == null) return
        var wins = 0
        var draws = 0
        var loses = 0

        playerStats.stats.forEach { (matchId, stats) ->
            if (stats.gamesPlayed != 1) return@forEach
            val match = matches.find { it.id == matchId } ?: return@forEach
            val local = match.localTeam.id == appTeam.id
            when (local) {
                true -> {
                    when {
                        match.localGoals > match.visitorGoals -> wins++
                        match.localGoals < match.visitorGoals -> loses++
                        else -> draws++
                    }
                }

                else -> {
                    when {
                        match.localGoals < match.visitorGoals -> wins++
                        match.localGoals > match.visitorGoals -> loses++
                        else -> draws++
                    }
                }
            }
        }

        _state.update {
            it.copy(
                winRate = PlayerWinRate(wins, draws, loses)
            )
        }
    }

    internal fun updateState(
        playerId: String,
        isManager: Boolean
    ) {
        viewModelScope.launch {
            val playerInfo = withContext(Dispatchers.IO) {
                fetchPlayerInformation(playerId)
            }

            val playerStats = withContext(Dispatchers.IO) {
                fetchPlayerStats(playerId)
            }
            val matches = withContext(Dispatchers.IO) {
                fetchMatches()
            }

            playerStats?.let {
                calculateMatchesStats(GAZTELU_BIRA, matches, playerStats)
            }

            playerInfo?.let {
                _state.update {
                    it.copy(
                        isManager = isManager,
                        player = playerInfo.toGBPlayer()
                    )
                }
            }
        }
    }
}
