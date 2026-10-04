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

package com.sgale.gaztelubira.core.screens.home.tabs.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.migration.model.match.Match
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.Cup
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.DEFEAT
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.DRAW
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.VICTORY
import com.sgale.gaztelubira.core.domain.migration.model.team.Team
import com.sgale.gaztelubira.core.domain.migration.usecase.GetMatches
import com.sgale.gaztelubira.core.domain.migration.usecase.GetSquad
import com.sgale.gaztelubira.core.domain.migration.usecase.GetTeam
import com.sgale.gaztelubira.core.domain.migration.usecase.SelectedSeason
import com.sgale.gaztelubira.core.domain.utils.toDate
import com.sgale.gaztelubira.multiplatform.model.GBMatch
import com.sgale.gaztelubira.multiplatform.model.GBMatchResult
import com.sgale.gaztelubira.multiplatform.model.GBMatchTeam
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.CUP
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.LEAGUE
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.MatchesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val KEEP_ALIVE_MILLIS = 5_000L
private const val MINIMUM_SQUAD = 11

@HiltViewModel
internal class MatchesViewModel @Inject constructor(
    selectedSeason: SelectedSeason,
    getMatches: GetMatches,
    getSquad: GetSquad,
    getTeam: GetTeam
) : ViewModel() {
    private val isAdmin = MutableStateFlow(false)
    private val season = selectedSeason()

    private val matches = season.map { season ->
        season?.let { getMatches(it) }.orEmpty()
    }

    private val hasEnoughPlayers = season.map { season ->
        val squad = season?.let { getSquad(it) }.orEmpty()
        squad.count { it.position != MANAGER } >= MINIMUM_SQUAD
    }

    internal val state: StateFlow<MatchesUiState> =
        combine(
            matches,
            hasEnoughPlayers,
            isAdmin
        ) { matches, enoughPlayers, admin ->
            MatchesUiState(
                matches = matches
                    .sortedByDescending { it.information.date }
                    .map { match ->
                        val local = getTeam(match.match.localTeam)
                        val visitor = getTeam(match.match.visitorTeam)
                        match.asGBMatch(local, visitor)
                    },
                isAdmin = admin,
                hasEnoughPlayers = enoughPlayers
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(KEEP_ALIVE_MILLIS),
            initialValue = MatchesUiState()
        )

    internal fun onAdminChanged(isAdmin: Boolean) {
        this.isAdmin.value = isAdmin
    }

    private fun Match.asGBMatch(
        local: Team,
        visitor: Team
    ) = GBMatch(
        id = id.value,
        name = getName(),
        type = when (competition) {
            is Cup -> CUP
            is League -> LEAGUE
        },
        // The domain keeps dates in seconds, toDate() expects millis
        date = (information.date * 1_000).toDate(),
        localTeam = getTeam(local, match.score.local),
        visitorTeam = getTeam(visitor, match.score.visitor),
        result = result().toGBMatchResult()
    )

    private fun getTeam(team: Team, goals: Int): GBMatchTeam =
        GBMatchTeam(
            name = team.name,
            logo = team.logo,
            goals = goals
        )

    // TODO
    private fun Match.getName(): String =
        when (competition) {
            is Cup -> "Cup"
            is League -> "Jornada ${(competition as League).journey}"
        }

    private fun MatchResult.toGBMatchResult() =
        when (this) {
            DEFEAT -> GBMatchResult.DEFEAT
            DRAW -> GBMatchResult.DRAW
            VICTORY -> GBMatchResult.VICTORY
        }
}
