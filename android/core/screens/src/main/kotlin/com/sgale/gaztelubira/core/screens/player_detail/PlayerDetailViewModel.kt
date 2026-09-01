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

package com.sgale.gaztelubira.core.screens.player_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.usecase.db.FetchMatches
import com.sgale.gaztelubira.core.domain.usecase.firestore.FetchPlayer
import com.sgale.gaztelubira.core.domain.usecase.firestore.FetchPlayerStats
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class PlayerDetailViewModel @Inject constructor(
    private val fetchPlayerInformation: FetchPlayer,
    private val fetchPlayerStats: FetchPlayerStats,
    private val fetchMatches: FetchMatches
) : ViewModel(), PlayerDetailContract {
    private val _playerState = MutableStateFlow<PlayerDetailState?>(null)
    override val playerState: StateFlow<PlayerDetailState?> = _playerState

    override fun calculateMatchesStats(
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

        _playerState.value = PlayerDetailState(
            wins = wins,
            draws = draws,
            loses = loses
        )
    }

    private val _playerInformation = MutableStateFlow<PlayerModel?>(null)
    val playerInformation = _playerInformation

    private val _playerStats = MutableStateFlow<PlayerStatsModel?>(null)
    val playerStats = _playerStats

    fun loadPlayerInformation(
        appTeam: TeamModel?,
        playerId: String
    ) {
        viewModelScope.launch {
            _playerInformation.value = withContext(Dispatchers.IO) {
                fetchPlayerInformation(playerId)
            }
            _playerStats.value = withContext(Dispatchers.IO) {
                fetchPlayerStats(playerId)
            }
            val matches = withContext(Dispatchers.IO) {
                fetchMatches()
            }

            calculateMatchesStats(appTeam, matches, _playerStats.value!!)
        }
    }
}
