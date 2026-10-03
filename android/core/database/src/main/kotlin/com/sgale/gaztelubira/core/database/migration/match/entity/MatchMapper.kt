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

package com.sgale.gaztelubira.core.database.migration.match.entity

import com.sgale.gaztelubira.core.database.DatabaseMapper
import com.sgale.gaztelubira.core.domain.migration.model.match.Match
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.Companion.asMatchType
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.Companion.resolveJourney
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.Companion.resolveName
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.Companion.resolveRound
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.Cup
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchInformation
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchScore
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchScore.Companion.isGBLocal
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchType
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchType.CUP
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchType.LEAGUE
import com.sgale.gaztelubira.core.domain.migration.model.match.Score
import com.sgale.gaztelubira.core.domain.migration.model.team.TeamFormation

internal object MatchMapper: DatabaseMapper<Match, MatchEntity> {
    override fun Match.asEntity(): MatchEntity =
        MatchEntity(
            id = id,
            seasonId = seasonId,
            date = information.date,
            description = information.description,
            location = information.location,
            journey = competition.resolveJourney(),
            name = competition.resolveName(),
            round = competition.resolveRound(),
            type = competition.asMatchType().name,
            formation = match.formation.name,
            localGoals = match.score.local,
            localTeam = match.localTeam,
            visitorGoals = match.score.visitor,
            visitorTeam = match.visitorTeam
        )

    override fun MatchEntity.asModel(): Match =
        Match(
            id = id,
            seasonId = seasonId,
            competition = toCompetition(),
            information = toInformation(),
            match = toMatch()
        )

    private fun MatchEntity.toCompetition(): MatchCompetition {
        val matchType = MatchType.entries.find { it.name == type }
            ?: if (name.isNotBlank()) CUP else LEAGUE

        return when (matchType) {
            CUP -> Cup(name, round)
            LEAGUE -> League(journey)
        }
    }

    private fun MatchEntity.toInformation(): MatchInformation =
        MatchInformation(
            date = date,
            description = description,
            location = location
        )

    private fun MatchEntity.toMatch(): MatchScore =
        MatchScore(
            formation = TeamFormation.valueOf(formation),
            isLocal = localTeam.isGBLocal(),
            localTeam = localTeam,
            visitorTeam = visitorTeam,
            score = Score(localGoals, visitorGoals)
        )
}