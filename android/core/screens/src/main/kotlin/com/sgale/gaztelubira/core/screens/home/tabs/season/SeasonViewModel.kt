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

package com.sgale.gaztelubira.core.screens.home.tabs.season

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.legacy.model.utils.GazteluBiraUtils
import com.sgale.gaztelubira.core.domain.legacy.model.utils.GazteluBiraUtils.GAZTELU_BIRA_ID
import com.sgale.gaztelubira.core.domain.migration.model.team.Team
import com.sgale.gaztelubira.core.domain.migration.usecase.GetMatches
import com.sgale.gaztelubira.core.domain.migration.usecase.GetTeams
import com.sgale.gaztelubira.core.domain.migration.usecase.SelectedSeason
import com.sgale.gaztelubira.core.screens.toGBMatchResult
import com.sgale.gaztelubira.multiplatform.model.GBSeason
import com.sgale.gaztelubira.multiplatform.model.GBTeam
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.season.SeasonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val KEEP_ALIVE_MILLIS = 5_000L

@HiltViewModel
internal class SeasonViewModel @Inject constructor(
    selectedSeason: SelectedSeason,
    getTeams: GetTeams,
    getMatches: GetMatches,
) : ViewModel() {
    private val isAdmin = MutableStateFlow(false)
    private val season = selectedSeason()

    private val matches = season.map { season ->
        season?.let { getMatches(it) }.orEmpty()
    }

    private val teams = season.map { season ->
        season?.let { getTeams(it) }.orEmpty()
    }

    private val _state: StateFlow<SeasonUiState> =
        combine(
            matches,
            teams,
            isAdmin
        ) { matches, teams, admin ->
            SeasonUiState(
                season = SeasonHandler(matches).getGBInformation().toGBSeason(),
                teams = teams.map { it.toGBTeam() }.filter { it.id != GAZTELU_BIRA_ID },
                isAdmin = admin
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(KEEP_ALIVE_MILLIS),
            initialValue = SeasonUiState()
        )

    internal val state: StateFlow<SeasonUiState> = _state

    internal fun onAdminChanged(isAdmin: Boolean) {
        this.isAdmin.value = isAdmin
    }

    private fun Team.toGBTeam(): GBTeam =
        GBTeam(
            id = id.value,
            name = name,
            logo = logo
        )

    private fun TeamSeason.toGBSeason(): GBSeason =
        GBSeason(
            points = points,
            games = games,
            wins = wins,
            draws = draws,
            loses = loses,
            goalsFor = goalsFor,
            goalsAgainst = goalsAgainst,
            currentStreak = streak.currentStreak,
            lastGames = streak.lastGames.map { it.toGBMatchResult() }
        )
}
