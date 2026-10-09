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

package com.sgale.gaztelubira.core.screens.detail.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.match.MatchScore
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.usecase.GetMatches
import com.sgale.gaztelubira.core.domain.usecase.GetPlayerStats
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.player.PlayerDetailUiState.PlayerWinRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class PlayerDetailViewModel @Inject constructor(
    private val getPlayerDetailInformation: GetPlayerDetailInformation,
    private val getPlayerStats: GetPlayerStats,
    private val getMatches: GetMatches
) : ViewModel() {
    private val _state = MutableStateFlow(PlayerDetailUiState())
    internal val state: StateFlow<PlayerDetailUiState> = _state

    internal fun updateState(
        playerId: PlayerId,
        isManager: Boolean
    ) {
        viewModelScope.launch {
            val playerInfo = withContext(IO) { getPlayerDetailInformation(playerId) } ?: return@launch
            val winRate = withContext(IO) { winRate(playerId) }

            _state.update {
                it.copy(
                    isManager = isManager,
                    player = playerInfo.toGBPlayer(),
                    winRate = winRate
                )
            }
        }
    }

    /**
     * Results of the season matches the player has stats for, i.e. the ones they played.
     */
    private suspend fun winRate(playerId: PlayerId): PlayerWinRate {
        val playedMatchIds = getPlayerStats(playerId).mapTo(HashSet()) { it.matchId }
        val goalDifferences = getMatches()
            .filter { it.id in playedMatchIds }
            .map { it.match.goalDifference() }

        return PlayerWinRate(
            wins = goalDifferences.count { it > 0 },
            draws = goalDifferences.count { it == 0 },
            loses = goalDifferences.count { it < 0 }
        )
    }

    /**
     * Seen from Gaztelu Bira's side: positive when it won, whether it played home or away.
     */
    private fun MatchScore.goalDifference(): Int =
        if (isLocal) score.local - score.visitor else score.visitor - score.local
}
