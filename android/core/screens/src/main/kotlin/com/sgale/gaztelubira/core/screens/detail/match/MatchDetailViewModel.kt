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

package com.sgale.gaztelubira.core.screens.detail.match

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.common.utils.TAG
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.usecase.GetMatchDetail
import com.sgale.gaztelubira.core.domain.migration.usecase.GetMatchPlayerStats
import com.sgale.gaztelubira.core.domain.migration.usecase.GetPlayers
import com.sgale.gaztelubira.core.domain.migration.usecase.GetTeam
import com.sgale.gaztelubira.core.domain.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.screens.mapper.MatchDetailUiMapper.toMatchDetailInformation
import com.sgale.gaztelubira.core.screens.mapper.MatchDetailUiMapper.toMatchDetailLineUp
import com.sgale.gaztelubira.core.screens.mapper.MatchDetailUiMapper.toMatchDetailStats
import com.sgale.gaztelubira.core.screens.mapper.TeamUiMapper.toGBTeam
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailState.Lineup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class MatchDetailViewModel @Inject constructor(
    private val getPlayers: GetPlayers,
    private val getMatchDetail: GetMatchDetail,
    private val getMatchPlayerStats: GetMatchPlayerStats,
    private val getTeam: GetTeam
) : ViewModel() {
    private val _state = MutableStateFlow(MatchDetailUiState(team = GAZTELU_BIRA.toGBTeam()))
    internal val state: StateFlow<MatchDetailUiState> = _state

    internal fun changeUiState(state: MatchDetailState) {
        _state.update { it.copy(uiState = state) }
    }

    internal fun loadMatch(
        matchId: MatchId,
        onMatchNotFound: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _state.value = withContext(IO) { loadState(matchId) }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading match: $e")
                onMatchNotFound()
            }
        }
    }

    private suspend fun loadState(matchId: MatchId): MatchDetailUiState {
        val players = getPlayers().associateBy { it.id }
        val matchDetail = getMatchDetail(matchId)
        val match = matchDetail.match.match
        val localTeam = getTeam(match.localTeam).toGBTeam()
        val visitorTeam = getTeam(match.visitorTeam).toGBTeam()

        val information = matchDetail.toMatchDetailInformation(localTeam, visitorTeam)
        val lineUp = matchDetail.toMatchDetailLineUp(players)
        val stats = getMatchPlayerStats(matchId).toMatchDetailStats(players)

        return MatchDetailUiState(
            uiState = Lineup(lineUp),
            team = GAZTELU_BIRA.toGBTeam(),
            localTeam = localTeam,
            localGoals = match.score.local,
            visitorTeam = visitorTeam,
            visitorGoals = match.score.visitor,
            information = information,
            lineUp = lineUp,
            stats = stats
        )
    }
}
