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

package com.sgale.gaztelubira.core.network.firebase.response.match

import com.sgale.gaztelubira.core.domain.model.match.Match.Companion.ERROR_MATCH
import com.sgale.gaztelubira.core.domain.model.match.MatchStats
import com.sgale.gaztelubira.core.domain.model.match.MatchStats.Companion.EMPTY_MATCH_STATS
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel
import com.sgale.gaztelubira.core.network.NetworkMapper
import com.sgale.gaztelubira.core.network.firebase.response.stats.StatsResponse

internal object MatchStatsMapper : NetworkMapper<MatchStatsModel, MatchStatsResponse> {
    override fun MatchStatsModel.asResponse() =
        MatchStatsResponse(
            id = id,
            location = location,
            description = description,
            formation = formation,
            lineUpPlayers = lineUpPlayers
                .mapKeys { it.key.toString() }
                .mapValues { it.value?.id ?: "" },
            benchPlayers = benchPlayers.map { it.id },
            managers = managers.map { it.id },
            stats = stats.asStatsMatchResponse()
        )

    override fun MatchStatsResponse.asModel() =
        MatchStatsModel(
            id = id,
            location = location,
            description = description,
            match = ERROR_MATCH, // TODO
            formation = formation,
            lineUpPlayers = emptyMap(), // TODO lineUpPlayers,
            benchPlayers = emptyList(), // TODO benchPlayers,
            managers = emptyList(), // TODO managers,
            stats = EMPTY_MATCH_STATS // TODO stats
        )

    private fun MatchStats.asStatsMatchResponse() =
        StatsResponse(
            assists = assists.map { it.id },
            cleanSheets = cleanSheets.map { it.id },
            fails = fails.map { it.id },
            goals = goals.map { it.id },
            goalsProvoked = goalsProvoked.map { it.id },
            penaltiesProvoked = penaltiesProvoked.map { it.id },
            redCards = redCards.map { it.id },
            saves = saves.map { it.id },
            yellowCards = yellowCards.map { it.id }
        )
}
