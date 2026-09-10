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

package com.sgale.gaztelubira.core.screens.detail.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerMapper.toGBPlayer
import com.sgale.gaztelubira.core.domain.model.team.TeamMapper.toGBTeam
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.domain.usecase.firestore.FetchMatchStats
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailInformation
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailLineUp
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Lineup
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class MatchDetailViewModel @Inject constructor(
    private val fetchMatchStats: FetchMatchStats
) : ViewModel() {

    private val gazteluBira = GAZTELU_BIRA.toGBTeam()
    private val _state = MutableStateFlow(MatchDetailUiState(team = gazteluBira))
    internal val state: StateFlow<MatchDetailUiState> = _state

    fun changeUiState(state: MatchDetailState) {
        _state.value = _state.value.copy(
            uiState = state
        )
    }

    fun loadMatch(
        matchId: FirebaseId,
        onMatchNotFound: () -> Unit
    ) {
        viewModelScope.launch {
            val matchStats = withContext(Dispatchers.IO) {
                fetchMatchStats(matchId)
            }

            if (matchStats != null) {
                createStateFromStats(matchStats)
            } else {
                onMatchNotFound()
            }
        }
    }

    private fun createStateFromStats(matchStats: MatchStatsModel) {
        val matchInformation = setMatchInformation(matchStats)
        val lineUp = setLineUp(matchStats)
        val stats = setStats(matchStats)

        _state.value = _state.value.copy(
            uiState = Lineup(lineUp),
            localTeam = matchStats.matchModel.localTeam.toGBTeam(),
            localGoals = matchStats.matchModel.localGoals,
            visitorTeam = matchStats.matchModel.visitorTeam.toGBTeam(),
            visitorGoals = matchStats.matchModel.visitorGoals,
            information = matchInformation,
            lineUp = lineUp,
            stats = stats
        )
    }

    private fun setMatchInformation(
        matchStats: MatchStatsModel
    ) = MatchDetailInformation(
        local = matchStats.matchModel.localTeam.toGBTeam(),
        visitor = matchStats.matchModel.visitorTeam.toGBTeam(),
        date = matchStats.matchModel.date,
        description = matchStats.description,
        location = matchStats.location
    )


    private fun setLineUp(
        matchStats: MatchStatsModel
    ) = MatchDetailLineUp(
        benchPlayers = matchStats.benchPlayers.map { it.toGBPlayer() },
        managers = matchStats.managers.map { it.toGBPlayer() },
        matchFormation = LineUpFormation.fromCode(matchStats.formation) ?: LineUpFormation.DEFAULT,
        players = matchStats.lineUpPlayers.mapValues { (_, player) -> player?.toGBPlayer() }
    )


    private fun setStats(
        matchStats: MatchStatsModel
    ) = MatchDetailStats(
        assists = matchStats.stats.assists.map { it.toGBPlayer() },
        cleanSheets = matchStats.stats.cleanSheets.map { it.toGBPlayer() },
        fails = matchStats.stats.fails.map { it.toGBPlayer() },
        goals = matchStats.stats.goals.map { it.toGBPlayer() },
        goalsProvoked = matchStats.stats.goalsProvoked.map { it.toGBPlayer() },
        penaltiesProvoked = matchStats.stats.penaltiesProvoked.map { it.toGBPlayer() },
        redCards = matchStats.stats.redCards.map { it.toGBPlayer() },
        saves = matchStats.stats.saves.map { it.toGBPlayer() },
        yellowCards = matchStats.stats.yellowCards.map { it.toGBPlayer() }
    )
}
