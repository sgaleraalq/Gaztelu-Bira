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

package com.sgale.gaztelubira.core.screens.mapper

import com.sgale.gaztelubira.core.domain.model.match.Match
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.Cup
import com.sgale.gaztelubira.core.domain.model.match.MatchCompetition.League
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.DEFEAT
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.DRAW
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.VICTORY
import com.sgale.gaztelubira.core.domain.model.team.Team
import com.sgale.gaztelubira.core.domain.model.team.TeamFormation
import com.sgale.gaztelubira.core.domain.model.team.TeamFormation.FIVE_THREE_TWO
import com.sgale.gaztelubira.core.domain.model.team.TeamFormation.FOUR_FOUR_TWO
import com.sgale.gaztelubira.core.domain.utils.GazteluBiraUtils.GAZTELU_BIRA_ID
import com.sgale.gaztelubira.core.domain.utils.toDate
import com.sgale.gaztelubira.multiplatform.designsystem.model.LineUpFormation
import com.sgale.gaztelubira.multiplatform.model.GBMatch
import com.sgale.gaztelubira.multiplatform.model.GBMatchResult
import com.sgale.gaztelubira.multiplatform.model.GBMatchTeam
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.CUP
import com.sgale.gaztelubira.multiplatform.model.GBMatchType.LEAGUE

internal object MatchUiMapper {
    fun Match.toGBMatch(
        local: Team,
        visitor: Team
    ): GBMatch =
        GBMatch(
            id = id.value,
            name = displayName(),
            type = when (competition) {
                is Cup -> CUP
                is League -> LEAGUE
            },
            // The domain keeps dates in seconds, toDate() expects millis
            date = (information.date * 1_000).toDate(),
            localTeam = local.toGBMatchTeam(match.score.local),
            visitorTeam = visitor.toGBMatchTeam(match.score.visitor),
            result = result().toGBMatchResult()
        )

    fun MatchResult.toGBMatchResult(): GBMatchResult =
        when (this) {
            DEFEAT -> GBMatchResult.DEFEAT
            DRAW -> GBMatchResult.DRAW
            VICTORY -> GBMatchResult.VICTORY
        }

    fun TeamFormation.toLineUpFormation(): LineUpFormation =
        when (this) {
            FOUR_FOUR_TWO -> LineUpFormation.FOUR_FOUR_TWO
            FIVE_THREE_TWO -> LineUpFormation.FIVE_THREE_TWO
        }

    private fun Team.toGBMatchTeam(goals: Int): GBMatchTeam =
        GBMatchTeam(
            isGaztelu = id.value == GAZTELU_BIRA_ID,
            name = name,
            logo = logo,
            goals = goals
        )

    // TODO
    private fun Match.displayName(): String =
        when (val competition = competition) {
            is Cup -> "Cup"
            is League -> "Jornada ${competition.journey}"
        }
}
