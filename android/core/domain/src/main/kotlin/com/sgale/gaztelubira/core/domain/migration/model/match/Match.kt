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

package com.sgale.gaztelubira.core.domain.migration.model.match

import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.DEFEAT
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.DRAW
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.VICTORY
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId

data class Match(
    val id: MatchId,
    val seasonId: SeasonId,
    val competition: MatchCompetition,
    val information: MatchInformation,
    val match: MatchScore
) {
    fun result(): MatchResult {
        val goalsFor = if (match.isLocal) match.score.local else match.score.visitor
        val goalsAgainst = if (match.isLocal) match.score.visitor else match.score.local

        return when {
            goalsFor > goalsAgainst -> VICTORY
            goalsFor < goalsAgainst -> DEFEAT
            else -> DRAW
        }
    }
}
