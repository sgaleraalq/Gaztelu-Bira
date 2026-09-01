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

package com.sgale.gaztelubira.core.screens.match_detail

import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.screens.match_detail.MatchDetailState.Loading
import com.sgale.gaztelubira.core.screens.match_detail.states.information.MatchDetailInformation
import com.sgale.gaztelubira.core.screens.match_detail.states.line_up.MatchDetailLineUp
import com.sgale.gaztelubira.core.screens.match_detail.states.stats.MatchDetailStats

data class MatchDetailUiState(
    val uiState: MatchDetailState = Loading,
    val localTeam: TeamModel? = null,
    val localGoals: Int = 0,
    val visitorTeam: TeamModel? = null,
    val visitorGoals: Int = 0,
    val information: MatchDetailInformation? = null,
    val lineUp: MatchDetailLineUp? = null,
    val stats: MatchDetailStats? = null
)
