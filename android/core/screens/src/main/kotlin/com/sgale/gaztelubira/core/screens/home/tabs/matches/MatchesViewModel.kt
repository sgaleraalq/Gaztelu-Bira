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
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.TESTING
import com.sgale.gaztelubira.core.preview.MatchProvider.provideMatchesList
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersDb
import com.sgale.gaztelubira.core.domain.usecase.db.GetMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val getMatches: GetMatches,
    private val playersDb: IGBPlayersDb
) : ViewModel() {
    private val _matches = MutableStateFlow<List<MatchModel>>(emptyList())
    val matches: StateFlow<List<MatchModel>> = _matches

    private val _enoughPlayers = MutableStateFlow(false)
    val enoughPlayers: StateFlow<Boolean> = _enoughPlayers

    init {
        viewModelScope.launch {
            val testFlow = if (TESTING) flowOf(provideMatchesList(20)) else flowOf(emptyList())

            hasEnoughPlayers()
            getMatches()
                .combine(testFlow) { real, test -> real + test }
                .flowOn(Dispatchers.IO)
                .collect { combined ->
                    _matches.value = combined
                }
        }
    }

    suspend fun hasEnoughPlayers() {
        _enoughPlayers.value = withContext(Dispatchers.IO) {
            playersDb.getNumberOfPlayers() >= 11
        }
    }
}
