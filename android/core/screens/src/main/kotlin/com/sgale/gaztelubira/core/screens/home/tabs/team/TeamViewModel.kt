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
import com.sgale.gaztelubira.core.domain.migration.model.player.Player
import com.sgale.gaztelubira.core.domain.migration.repository.player.PlayerLocal
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TeamViewModel @Inject constructor(
    private val playerLocal: PlayerLocal
) : ViewModel() {

    private val _state = MutableStateFlow(TeamUiState())
    internal val state: StateFlow<TeamUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val players = playerLocal.getPlayers().map { it.asGBPlayer() }
            _state.update { it.copy(players = players) }
        }
    }

    internal fun onAdminChanged(isAdmin: Boolean) {
        _state.update { it.copy(isAdmin = isAdmin) }
    }

    private fun Player.asGBPlayer() =
        GBPlayer(
            id = id.value,
            name = name,
            image = faceImage
        )
}
