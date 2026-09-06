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

package com.sgale.gaztelubira.core.screens.home.tabs.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.match.MatchMapper.toGBMatch
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.DEFEAT
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.DRAW
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.UNDEFINED
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.VICTORY
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.TESTING
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersDb
import com.sgale.gaztelubira.core.domain.usecase.db.GetMatches
import com.sgale.gaztelubira.core.preview.MatchProvider.provideMatchesList
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.MatchesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val MINIMUM_SQUAD = 11

@HiltViewModel
internal class MatchesViewModel @Inject constructor(
    private val getMatches: GetMatches,
    private val playersDb: IGBPlayersDb
) : ViewModel() {
    private val _state = MutableStateFlow(MatchesUiState())
    internal val state: StateFlow<MatchesUiState> = _state.asStateFlow()

    private var appTeam: TeamModel? = null
    private var matches: List<MatchModel> = emptyList()

    init {
        viewModelScope.launch {
            val hasEnoughPlayers = withContext(IO) {
                playersDb.getNumberOfPlayers() >= MINIMUM_SQUAD
            }

            updateInsertMatchAvailability(hasEnoughPlayers)
        }

        viewModelScope.launch {
            val testFlow = if (TESTING) flowOf(provideMatchesList(20)) else flowOf(emptyList())

            getMatches()
                .combine(testFlow) { real, test -> real + test }
                .flowOn(IO)
                .collect { combined ->
                    matches = combined.sortedByDescending { it.date }
                    renderMatches()
                }
        }
    }

    private fun updateInsertMatchAvailability(enabled: Boolean) {
        _state.update { it.copy(hasEnoughPlayers = enabled) }
    }

    internal fun onSessionChanged(team: TeamModel?, isAdmin: Boolean) {
        appTeam = team
        _state.update { it.copy(isAdmin = isAdmin) }
        renderMatches()
    }

    private fun renderMatches() {
        val team = appTeam
        _state.update { state ->
            state.copy(
                matches = matches.map { match ->
                    match.toGBMatch(team, getMatchResult(match, team))
                }
            )
        }
    }

    private fun getMatchResult(
        match: MatchModel,
        appTeam: TeamModel?
    ): MatchResult {
        if (appTeam == null) return UNDEFINED

        val isLocal = match.localTeam.id == appTeam.id
        val goalsFor = if (isLocal) match.localGoals else match.visitorGoals
        val goalsAgainst = if (isLocal) match.visitorGoals else match.localGoals

        return when {
            goalsFor > goalsAgainst -> VICTORY
            goalsFor < goalsAgainst -> DEFEAT
            else -> DRAW
        }
    }
}
