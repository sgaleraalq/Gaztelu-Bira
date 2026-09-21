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

package com.sgale.gaztelubira.core.domain.migration.model.season.match

import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.TeamId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.match.MatchSide
import com.sgale.gaztelubira.core.domain.model.match.MatchSide.LOCAL
import com.sgale.gaztelubira.core.domain.model.match.MatchSide.VISITOR

data class Match(
    val id: MatchId,
    val seasonId: SeasonId,
    val competition: Competition,
    val kickOffAt: Long,
    val localTeam: TeamId,
    val visitorTeam: TeamId,
    /** null until it is played: a scheduled match is not a 0-0 draw. */
    val score: Score?
) {
    val isPlayed: Boolean get() = score != null

    /** @return null when that team is not playing this match. */
    fun sideOf(team: TeamId): MatchSide? = when (team) {
        localTeam -> LOCAL
        visitorTeam -> VISITOR
        else -> null
    }

    /** @return null when the match has not been played, or the team is not in it. */
    fun resultFor(team: TeamId): MatchResult? {
        val side = sideOf(team) ?: return null
        return score?.resultFor(side)
    }
}
