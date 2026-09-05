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

package com.sgale.gaztelubira.core.screens.home.tabs.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Position.Manager
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.TESTING
import com.sgale.gaztelubira.core.domain.usecase.db.GetPlayers
import com.sgale.gaztelubira.core.preview.PlayerProvider.providePlayerInformationList
import com.sgale.gaztelubira.core.screens.toGBPlayer
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TeamViewModel @Inject constructor(
    private val getPlayers: GetPlayers
) : ViewModel() {

    private val _state = MutableStateFlow(TeamUiState())
    internal val state: StateFlow<TeamUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val testFlow =
                if (TESTING) flowOf(providePlayerInformationList()) else flowOf(emptyList())

            getPlayers()
                .combine(testFlow) { real, test -> real + test }
                .flowOn(Dispatchers.IO)
                .collect { squad -> _state.update { it.withSquad(squad) } }
        }
    }

    internal fun onAdminChanged(isAdmin: Boolean) {
        _state.update { it.copy(isAdmin = isAdmin) }
    }
}

private fun TeamUiState.withSquad(squad: List<PlayerModel>): TeamUiState {
    val (managers, players) = squad.partition { it.position == Manager }

    return copy(
        players = players
            .filter { it.dorsal != null }
            .sortedBy { it.dorsal }
            .map(PlayerModel::toGBPlayer),
        managers = managers
            .sortedBy { it.name }
            .map(PlayerModel::toGBPlayer)
    )
}
