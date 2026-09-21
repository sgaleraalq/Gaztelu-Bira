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

package com.sgale.gaztelubira.core.domain.model.match

import com.sgale.gaztelubira.core.domain.model.match.MatchType.LEAGUE
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.team.Team.Companion.ERROR_TEAM
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId

data class Match(
    val id: FirebaseId,
    val date: Long,
    val matchName: String,
    val matchType: MatchType,
    val localTeam: Team,
    val visitorTeam: Team,
    val localGoals: Int,
    val visitorGoals: Int
) {
    companion object {
        val ERROR_MATCH = Match(
            id = "error_team",
            date = 0L,
            matchName = "Error Match",
            matchType = LEAGUE,
            localTeam = ERROR_TEAM,
            visitorTeam = ERROR_TEAM,
            localGoals = 0,
            visitorGoals = 0
        )
    }
}
