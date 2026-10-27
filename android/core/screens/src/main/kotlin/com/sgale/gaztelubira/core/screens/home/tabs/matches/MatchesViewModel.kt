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
import com.sgale.gaztelubira.core.domain.auth.SessionManager
import com.sgale.gaztelubira.core.domain.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.usecase.GetMatches
import com.sgale.gaztelubira.core.domain.usecase.GetSquad
import com.sgale.gaztelubira.core.domain.usecase.GetTeam
import com.sgale.gaztelubira.core.domain.usecase.SelectedSeason
import com.sgale.gaztelubira.core.screens.mapper.MatchUiMapper.toGBMatch
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.matches.MatchesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    sessionManager: SessionManager,
    selectedSeason: SelectedSeason,
    getMatches: GetMatches,
    getSquad: GetSquad,
    getTeam: GetTeam
) : ViewModel() {
    private val isAdmin = sessionManager.isAdmin
    private val season = selectedSeason()

    private val matches = season.map { season ->
        season?.let { getMatches(it) }.orEmpty()
    }

    private val hasEnoughPlayers = season.map { season ->
        val squad = season?.let { getSquad(it) }.orEmpty()
        squad.count { it.position != MANAGER } >= MINIMUM_SQUAD
    }

    private val _state: StateFlow<MatchesUiState> =
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
                        match.toGBMatch(local, visitor)
                    },
                isAdmin = admin,
                hasEnoughPlayers = enoughPlayers
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(KEEP_ALIVE_MILLIS),
            initialValue = MatchesUiState()
        )

    internal val state: StateFlow<MatchesUiState> = _state
}
