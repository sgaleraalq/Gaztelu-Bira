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

package com.sgale.gaztelubira.core.database.migration.mapper

import com.sgale.gaztelubira.core.database.DatabaseMapper
import com.sgale.gaztelubira.core.database.migration.entity.MatchLineupEntity
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchLineup
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchPlayerState.UNDEFINED

internal object MatchLineupMapper : DatabaseMapper<MatchLineup, MatchLineupEntity> {
    override fun MatchLineup.asEntity(): MatchLineupEntity =
        MatchLineupEntity(
            matchId = matchId,
            playerId = playerId,
            seasonId = seasonId,
            state = state.name,
            formationPosition = formationPosition
        )

    override fun MatchLineupEntity.asModel(): MatchLineup =
        MatchLineup(
            playerId = playerId,
            matchId = matchId,
            seasonId = seasonId,
            state = MatchPlayerState.entries.find { it.name == state } ?: UNDEFINED,
            formationPosition = formationPosition
        )
}
