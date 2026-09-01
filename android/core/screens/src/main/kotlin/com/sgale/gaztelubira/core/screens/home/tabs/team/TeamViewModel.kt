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

package com.sgale.gaztelubira.core.screens.home.tabs.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.TESTING
import com.sgale.gaztelubira.core.preview.PlayerProvider.providePlayerInformationList
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.usecase.db.GetPlayers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getPlayers: GetPlayers
) : ViewModel() {
    private val _players = MutableStateFlow<List<PlayerModel>>(emptyList())
    val players = _players

    init {
        viewModelScope.launch {
            val testFlow = if (TESTING) flowOf(providePlayerInformationList()) else flowOf(emptyList())

            getPlayers()
                .combine(testFlow) { real, test -> real + test }
                .flowOn(Dispatchers.IO)
                .collect { combined ->
                    _players.value = combined
                        .sortedBy { it.dorsal }
                        .filter { it.dorsal != null }
                }
        }
    }
}
