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

package com.sgale.gaztelubira.core.screens.home.tabs.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.migration.model.season.squad.SquadPlayer
import com.sgale.gaztelubira.core.domain.migration.usecase.season.GetSquad
import com.sgale.gaztelubira.core.domain.migration.usecase.season.SelectedSeason
import com.sgale.gaztelubira.multiplatform.model.GBPlayer
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val KEEP_ALIVE_MILLIS = 5_000L

@HiltViewModel
internal class TeamViewModel @Inject constructor(
    selectedSeason: SelectedSeason,
    getSquad: GetSquad
) : ViewModel() {

    private val isAdmin = MutableStateFlow(false)

    private val squad = selectedSeason().map { season ->
        season?.let { getSquad(it) }.orEmpty()
    }

    internal val state: StateFlow<TeamUiState> =
        combine(
            squad,
            isAdmin
        ) { squad, admin ->
            TeamUiState(
                players = squad.filterNot { it.position == MANAGER }.map { it.asGBPlayer() },
                managers = squad.filter { it.position == MANAGER }.map { it.asGBPlayer() },
                isAdmin = admin
            )
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(KEEP_ALIVE_MILLIS),
            initialValue = TeamUiState()
        )

    internal fun onAdminChanged(isAdmin: Boolean) {
        this.isAdmin.value = isAdmin
    }

    private fun SquadPlayer.asGBPlayer() =
        GBPlayer(
            id = player.id.value,
            name = player.nickname ?: player.name,
            image = player.faceImage,
            dorsal = dorsal
        )
}
