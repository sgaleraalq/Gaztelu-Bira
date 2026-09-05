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

package com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.TESTING
import com.sgale.gaztelubira.core.preview.MatchProvider.provideMatchesList
import com.sgale.gaztelubira.core.preview.TeamProvider.provideRandomTeams
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.usecase.db.GetMatches
import com.sgale.gaztelubira.core.domain.usecase.db.GetTeams
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

@HiltViewModel
class GazteluBiraHomeViewModel @Inject constructor(
    private val getTeams: GetTeams,
    private val getMatches: GetMatches
): ViewModel() {

    private val _gbInformation = MutableStateFlow<GBInformation?>(null)
    val gbInformation: StateFlow<GBInformation?> = _gbInformation

    private val _teams = MutableStateFlow<List<TeamModel>>(emptyList())
    val teams: StateFlow<List<TeamModel>> = _teams

    private val matches = MutableStateFlow<List<MatchModel>>(emptyList())
    private var appTeam: TeamModel? = null
    private var gbHomeHandler: GazteluBiraHomeHandler? = null

    init {
        viewModelScope.launch {
            val testTeams = if (TESTING) flowOf(provideRandomTeams(20)) else flowOf(emptyList())
            val testMatches = if (TESTING) flowOf(provideMatchesList(20)) else flowOf(emptyList())

            launch {
                getTeams()
                    .combine(testTeams) { real, test -> real + test }
                    .flowOn(Dispatchers.IO)
                    .collect { teamsList ->
                        _teams.value = teamsList
                    }
            }

            launch {
                getMatches()
                    .combine(testMatches) { real, test -> real + test }
                    .flowOn(Dispatchers.IO)
                    .collect { matchesList ->
                        matches.value = matchesList
                        recomputeGBInformation()
                    }
            }
        }
    }

    fun startHandler(appTeam: TeamModel) {
        this.appTeam = appTeam
        recomputeGBInformation()
    }

    private fun recomputeGBInformation() {
        val team = appTeam ?: return
        val matches = matches.value

        gbHomeHandler = GazteluBiraHomeHandler(
            appTeam = team,
            matches = matches
        )

        _gbInformation.value = gbHomeHandler!!.getGBInformation()
    }
}
