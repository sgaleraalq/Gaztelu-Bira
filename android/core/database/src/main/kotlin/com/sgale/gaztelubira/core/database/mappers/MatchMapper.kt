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

import com.sgale.gaztelubira.core.database.db.entities.MatchEntity
import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchType
import com.sgale.gaztelubira.core.domain.model.team.Team.Companion.ERROR_TEAM

internal object MatchMapper : DatabaseMapper<Match, MatchEntity> {
    override fun Match.asEntity() =
        MatchEntity(
            id = id,
            date = date,
            matchName = matchName,
            matchType = matchType.name,
            localTeam = localTeam.id,
            localGoals = localGoals,
            visitorTeam = visitorTeam.id,
            visitorGoals = visitorGoals
        )

    override fun MatchEntity.asModel() =
        Match(
            id = id,
            date = date,
            matchName = matchName,
            matchType = MatchType.valueOf(matchType),
            localTeam = ERROR_TEAM, // TODO
            localGoals = localGoals,
            visitorTeam = ERROR_TEAM, // TODO
            visitorGoals = visitorGoals
        )
}
