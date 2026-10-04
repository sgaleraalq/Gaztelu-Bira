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
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchScore

internal class SeasonHandler(
    private val matches: List<Match>
) {
    private data class MatchResults(
        var wins: Int = 0,
        var draws: Int = 0,
        var loses: Int = 0
    )

    private data class MatchGoals(
        var goalsFor: Int = 0,
        var goalsAgainst: Int = 0
    )

    private data class MatchStreak(
        val games: MutableList<MatchResult> = mutableListOf()
    )

    private val results = MatchResults()
    private val goals = MatchGoals()
    private val streak = MatchStreak()


    init {
        matches.sortedBy { it.information.date }.forEach { match ->
            updateMatchResult(match.match)
            updateMatchGoals(match.match)
        }
    }

    private fun getCurrentStreak(): Int {
        if (streak.games.isEmpty()) return 0

        var count = 0
        for (result in streak.games.asReversed()) {
            if (result == VICTORY) {
                count++
            } else {
                break
            }
        }
        return count
    }

    internal fun getGBInformation(): TeamSeason =
        provideGBInformation()

    private fun getPoints(results: MatchResults): Int =
        (results.wins * 3) + results.draws

    private fun getStreak() =
        TeamStreak(
            currentStreak = getCurrentStreak(),
            lastGames = streak.games.toList()
        )

    private fun provideGBInformation(): TeamSeason {
        val points = getPoints(results)

        return TeamSeason(
            points = points,
            games = matches.size,
            wins = results.wins,
            draws = results.draws,
            loses = results.loses,
            goalsFor = goals.goalsFor,
            goalsAgainst = goals.goalsAgainst,
            streak = getStreak()
        )
    }

    private fun updateMatchGoals(
        match: MatchScore
    ) {
        when (match.isLocal) {
            true -> {
                goals.goalsFor += match.score.local
                goals.goalsAgainst += match.score.visitor
            }

            false -> {
                goals.goalsFor += match.score.visitor
                goals.goalsAgainst += match.score.local
            }
        }
    }

    private fun updateMatchResult(
        match: MatchScore
    ) {
        if (match.score.local == match.score.visitor) {
            streak.games.add(DRAW)
            results.draws += 1
            return
        }

        var streakResult = VICTORY
        when (match.isLocal) {
            true -> {
                if (match.score.local > match.score.visitor) {
                    results.wins += 1
                } else {
                    results.loses += 1
                    streakResult = DEFEAT
                }
            }

            false -> {
                if (match.score.local > match.score.visitor) {
                    results.loses += 1
                    streakResult = DEFEAT
                } else {
                    results.wins += 1
                }
            }
        }

        streak.games.add(streakResult)
    }
}
