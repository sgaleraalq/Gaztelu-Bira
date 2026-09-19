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

package com.sgale.gaztelubira.core.database.mappers

import com.sgale.gaztelubira.core.database.db.entities.MatchStatsEntity
import com.sgale.gaztelubira.core.database.db.entities.StatsMatchEntity
import com.sgale.gaztelubira.core.domain.model.match.Match.Companion.ERROR_MATCH
import com.sgale.gaztelubira.core.domain.model.match.MatchStats.Companion.EMPTY_MATCH_STATS
import com.sgale.gaztelubira.core.domain.model.match.MatchStatsModel

internal object MatchStatsMapper : DatabaseMapper<MatchStatsModel, MatchStatsEntity> {
    override fun MatchStatsModel.asEntity() =
        MatchStatsEntity(
            id = id,
            location = location,
            description = description,
            formation = formation,
            lineUpPlayers = lineUpPlayers.mapValues { it.value?.id.orEmpty() },
            benchPlayers = benchPlayers.map { it.id },
            managers = managers.map { it.id },
            stats = StatsMatchEntity() // TODO
        )

    override fun MatchStatsEntity.asModel() =
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
}
