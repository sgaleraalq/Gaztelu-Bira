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

package com.sgale.gaztelubira.core.network.migration.firebase.match

import com.google.firebase.Timestamp
import com.sgale.gaztelubira.core.domain.legacy.model.utils.GazteluBiraUtils.GAZTELU_BIRA_ID
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.match.Match
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.Cup
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchInformation
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchScore
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchType
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchType.CUP
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchType.LEAGUE
import com.sgale.gaztelubira.core.domain.migration.model.match.Score
import com.sgale.gaztelubira.core.network.migration.firebase.NetworkMapper

internal object MatchMapper: NetworkMapper<Match, MatchResponse, MatchId> {
    override fun Match.asResponse(): MatchResponse =
        MatchResponse(
            competition = competition.asResponse(),
            information = information.asResponse(),
            match = match.asResponse()
        )

    override fun MatchResponse.asModel(id: MatchId): Match =
        Match(
            id = id,
            competition = competition.asModel(),
            information = information.asModel(),
            match = match.asModel()
        )

    /**
     * As response
     */
    private fun MatchCompetition.asResponse(): MatchResponseCompetition =
        when (this) {
            is Cup -> MatchResponseCompetition(
                name = name,
                round = round,
                type = CUP.name
            )
            is League -> MatchResponseCompetition(
                journey = journey,
                type = LEAGUE.name
            )
        }

    private fun MatchInformation.asResponse(): MatchResponseInformation =
        MatchResponseInformation(
            date = Timestamp(date, 0),
            description = description,
            location = location
        )

    private fun MatchScore.asResponse(): MatchResponseScore =
        MatchResponseScore(
            formation = formation,
            localTeam = localTeam,
            visitorTeam = visitorTeam,
            score = MatchResponseScore.Score(score.local, score.visitor)
        )


    /**
     * As Model
     */
    private fun MatchResponseCompetition.asModel(): MatchCompetition {
        val matchType = MatchType.entries.find { it.name == type }
            ?: if (name != null) CUP else LEAGUE

        return when (matchType) {
            CUP -> Cup(
                name = name.orEmpty(),
                round = round.orEmpty()
            )
            LEAGUE -> League(
                journey = journey ?: 0
            )
        }
    }

    private fun MatchResponseInformation.asModel(): MatchInformation =
        MatchInformation(
            date = date?.seconds ?: 0L,
            description = description,
            location = location
        )

    private fun MatchResponseScore.asModel(): MatchScore =
        MatchScore(
            formation = formation,
            isLocal = localTeam.value == GAZTELU_BIRA_ID,
            visitorTeam = visitorTeam,
            localTeam = localTeam,
            score = score.asModel()
        )

    private fun MatchResponseScore.Score.asModel(): Score =
        Score(
            local = local,
            visitor = visitor
        )
}