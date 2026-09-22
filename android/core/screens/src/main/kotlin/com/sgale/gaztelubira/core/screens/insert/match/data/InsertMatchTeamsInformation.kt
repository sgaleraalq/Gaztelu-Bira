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

package com.sgale.gaztelubira.core.screens.insert.match.data

import com.sgale.gaztelubira.core.domain.legacy.model.match.Match
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchType
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchType.LEAGUE
import com.sgale.gaztelubira.core.domain.legacy.model.team.Team
import com.sgale.gaztelubira.core.domain.legacy.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.utils.getActualTimeAsLong

data class InsertMatchTeamsInformation(
    val id: FirebaseId = getActualTimeAsLong().toString(),
    val date: Long = getActualTimeAsLong(),
    val matchName: String = "",
    val matchType: MatchType = LEAGUE,
    val local: Team? = null,
    val localGoals: Int = -1,
    val visitor: Team? = null,
    val visitorGoals: Int = -1,
    val appTeamLocal: Boolean = true,
    val numberOfJourneys: Int = 0,
    val journeyName: String = "",
    val cupName: String = ""
) {
    fun getGoalsScored(): Int {
        return if (appTeamLocal) localGoals else visitorGoals
    }

    fun getGoalsReceived(): Int {
        return if (appTeamLocal) visitorGoals else localGoals
    }

    fun validMatch() = local != null && visitor != null

    fun toMatchModel() =
        Match(
            id = id,
            date = date,
            matchName = matchName,
            matchType = matchType,
            localTeam = local!!,
            visitorTeam = visitor!!,
            localGoals = localGoals,
            visitorGoals = visitorGoals
        )
}
