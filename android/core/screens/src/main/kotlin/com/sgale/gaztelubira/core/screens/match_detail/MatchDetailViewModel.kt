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

package com.sgale.gaztelubira.core.screens.match_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation.Companion.getLineUpFromString
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.usecase.firestore.FetchMatchStats
import com.sgale.gaztelubira.core.domain.utils.IToastManager
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailState.Lineup
import com.sgale.gaztelubira.core.screens.match_detail.states.information.MatchDetailInformation
import com.sgale.gaztelubira.core.screens.match_detail.states.line_up.MatchDetailLineUp
import com.sgale.gaztelubira.core.screens.match_detail.states.stats.MatchDetailStats
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val fetchMatchStats: FetchMatchStats,
    private val toastManager: IToastManager
) : ViewModel() {
    private val _data = MutableStateFlow(MatchDetailUiState())
    val data: StateFlow<MatchDetailUiState> = _data

    fun changeUiState(state: MatchDetailState) {
        _data.value = _data.value.copy(
            uiState = state
        )
    }

    fun loadMatch(state: NavigationState, matchId: FirebaseId) {
        viewModelScope.launch {
            val matchStats = withContext(Dispatchers.IO) {
                fetchMatchStats(matchId)
            }

            if (matchStats != null) {
                createStateFromStats(matchStats)
            } else {
                showErrorToast()
                state.navigateBack()
            }
        }
    }

    private fun createStateFromStats(matchStats: MatchStatsModel) {
        val matchInformation = setMatchInformation(matchStats)
        val lineUp = setLineUp(matchStats)
        val stats = setStats(matchStats)

        _data.value = _data.value.copy(
            uiState = Lineup(lineUp),
            localTeam = matchStats.matchModel.localTeam,
            localGoals = matchStats.matchModel.localGoals,
            visitorTeam = matchStats.matchModel.visitorTeam,
            visitorGoals = matchStats.matchModel.visitorGoals,
            information = matchInformation,
            lineUp = lineUp,
            stats = stats
        )
    }

    private fun setMatchInformation(
        matchStats: MatchStatsModel
    ) = MatchDetailInformation(
        local = matchStats.matchModel.localTeam,
        visitor = matchStats.matchModel.visitorTeam,
        date = matchStats.matchModel.date,
        description = matchStats.description,
        location = matchStats.location
    )


    private fun setLineUp(
        matchStats: MatchStatsModel
    ) = MatchDetailLineUp(
        benchPlayers = matchStats.benchPlayers,
        managers = matchStats.managers,
        matchFormation = getLineUpFromString(matchStats.formation),
        players = matchStats.lineUpPlayers
    )


    private fun setStats(
        matchStats: MatchStatsModel
    ) = MatchDetailStats(
        assists = matchStats.stats.assists,
        cleanSheets = matchStats.stats.cleanSheets,
        fails = matchStats.stats.fails,
        goals = matchStats.stats.goals,
        goalsProvoked = matchStats.stats.goalsProvoked,
        penaltiesProvoked = matchStats.stats.penaltiesProvoked,
        redCards = matchStats.stats.redCards,
        saves = matchStats.stats.saves,
        yellowCards = matchStats.stats.yellowCards
    )

    private fun showErrorToast() {
        toastManager.showToast("There was an error")
    }
}
