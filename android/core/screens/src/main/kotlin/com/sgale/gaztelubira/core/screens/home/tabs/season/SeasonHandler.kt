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

package com.sgale.gaztelubira.core.screens.home.tabs.season

import com.sgale.gaztelubira.core.domain.migration.model.match.Match
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.DEFEAT
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.DRAW
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchResult.VICTORY
import javax.inject.Inject

internal class SeasonHandler @Inject constructor() {
    internal fun getGBInformation(matches: List<Match>): TeamSeason {
        var wins = 0
        var draws = 0
        var loses = 0
        var goalsFor = 0
        var goalsAgainst = 0
        val games = mutableListOf<MatchResult>()

        matches.sortedBy { it.information.date }.forEach { match ->
            val score = match.match
            val (gbGoals, rivalGoals) =
                if (score.isLocal) score.score.local to score.score.visitor
                else score.score.visitor to score.score.local

            goalsFor += gbGoals
            goalsAgainst += rivalGoals

            val result = when {
                gbGoals > rivalGoals -> VICTORY.also { wins++ }
                gbGoals < rivalGoals -> DEFEAT.also { loses++ }
                else -> DRAW.also { draws++ }
            }
            games.add(result)
        }

        return TeamSeason(
            points = (wins * 3) + draws,
            games = matches.size,
            wins = wins,
            draws = draws,
            loses = loses,
            goalsFor = goalsFor,
            goalsAgainst = goalsAgainst,
            streak = TeamStreak(
                currentStreak = games.asReversed().takeWhile { it == VICTORY }.size,
                lastGames = games.toList()
            )
        )
    }
}
