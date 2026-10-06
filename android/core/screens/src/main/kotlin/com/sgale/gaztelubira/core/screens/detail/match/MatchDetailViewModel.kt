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
import com.sgale.gaztelubira.core.domain.legacy.model.utils.GazteluBiraUtils.GAZTELU_BIRA
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchDetail
import com.sgale.gaztelubira.core.domain.migration.model.team.Team
import com.sgale.gaztelubira.core.domain.migration.usecase.GetMatchDetail
import com.sgale.gaztelubira.core.domain.migration.usecase.GetTeam
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailUiState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.state.MatchDetailInformation
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
    private val getMatchDetail: GetMatchDetail,
    private val getTeam: GetTeam
) : ViewModel() {
    private val _state = MutableStateFlow(MatchDetailUiState(team = GAZTELU_BIRA.toGBTeam()))
    internal val state: StateFlow<MatchDetailUiState> = _state

    internal fun changeUiState(state: MatchDetailState) {
        _state.value = _state.value.copy(uiState = state)
    }

    internal fun loadMatch(
        matchId: MatchId,
        onMatchNotFound: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val matchDetail = withContext(IO) {
                    getMatchDetail(matchId)
                }

//                val players = withContext(IO) {
//                    getMatchPlayers(matchId)
//                }

                val localTeam = withContext(IO) {
                    getTeam(matchDetail.match.match.localTeam)
                }

                val visitorTeam = withContext(IO) {
                    getTeam(matchDetail.match.match.visitorTeam)
                }

                updateState(matchDetail, localTeam, visitorTeam)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading match: $e")
                onMatchNotFound()
            }
        }
    }

    private fun updateState(
        matchDetail: MatchDetail,
        localTeam: Team,
        visitorTeam: Team
    ) {
        _state.update {
            it.copy(
                team = GAZTELU_BIRA.toGBTeam(),
                localTeam = localTeam.toGBTeam(),
                visitorTeam = visitorTeam.toGBTeam(),
                localGoals = matchDetail.match.match.score.local,
                visitorGoals = matchDetail.match.match.score.visitor,
                information = matchDetail.toMatchDetailInformation(localTeam, visitorTeam),
                lineUp = null,
                stats = null
            )
        }

        _state.update {
            it.copy(uiState = Lineup(state.value.lineUp))
        }
    }

    private fun MatchDetail.toMatchDetailInformation(
        localTeam: Team,
        visitorTeam: Team
    ) = MatchDetailInformation(
        local = localTeam.toGBTeam(),
        visitor = visitorTeam.toGBTeam(),
        date = match.information.date,
        description = match.information.description,
        location = match.information.location
    )

    private fun Team.toGBTeam(): GBTeam =
        GBTeam(
            id = id.value,
            name = name,
            logo = logo
        )
}
