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

package com.sgale.gaztelubira.core.domain.model.team

import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.*
import com.sgale.gaztelubira.multiplatform.model.GBMatchResult
import com.sgale.gaztelubira.multiplatform.model.GBSeason
import com.sgale.gaztelubira.multiplatform.model.GBTeam

object TeamMapper {
    fun TeamSeason.toGBTeamSummary(): GBSeason =
        GBSeason(
            points = points,
            games = games,
            wins = wins,
            draws = draws,
            loses = loses,
            goalsFor = goalsFor,
            goalsAgainst = goalsAgainst,
            currentStreak = streak.currentStreak,
            lastGames = streak.lastGames.map { it.toGBMatchResult() }
        )

    fun TeamModel.toGBTeam(): GBTeam =
        GBTeam(
            id = id,
            name = name,
            logo = logo
        )

    fun MatchResult.toGBMatchResult(): GBMatchResult =
        when (this) {
            VICTORY -> GBMatchResult.VICTORY
            DRAW -> GBMatchResult.DRAW
            DEFEAT -> GBMatchResult.DEFEAT
            UNDEFINED -> GBMatchResult.UNDEFINED
        }
}
