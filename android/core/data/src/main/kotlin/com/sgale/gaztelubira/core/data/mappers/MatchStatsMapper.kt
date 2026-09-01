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

package com.sgale.gaztelubira.core.data.mappers

import com.sgale.gaztelubira.core.data.db.entities.MatchStatsEntity
import com.sgale.gaztelubira.core.data.network.response.MatchStatsResponse
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel

object MatchStatsMapper:
    Mapper<MatchStatsResponse, MatchStatsModel, MatchStatsEntity>
{
    override fun asResponse(domain: MatchStatsModel) =
        MatchStatsResponse(
            id = domain.id,
            location = domain.location,
            description = domain.description,
            formation = domain.formation,
            lineUpPlayers = domain.lineUpPlayers
                .mapKeys { it.key.toString() }
                .mapValues { it.value?.id ?: "" },
            benchPlayers = domain.benchPlayers.map { it.id },
            managers = domain.managers.map { it.id },
            stats = domain.stats.asStatsMatchResponse()
        )

    override fun asEntity(domain: MatchStatsModel) =
        MatchStatsEntity(
            id = domain.id,
            location = domain.location,
            description = domain.description,
            formation = domain.formation,
            lineUpPlayers = domain.lineUpPlayers.mapValues { it.value?.id ?: "" },
            benchPlayers = domain.benchPlayers.map { it.id },
            managers = domain.managers.map { it.id },
            stats = domain.stats.asStatsMatchEntity()
        )

    override fun entityAsDomain(
        entity: MatchStatsEntity
    ): MatchStatsModel? = null

    override fun responseAsModel(
        response: MatchStatsResponse
    ): MatchStatsModel? = null
}