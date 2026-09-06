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

package com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira

import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.DEFEAT
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.DRAW
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.VICTORY
import com.sgale.gaztelubira.core.domain.model.match.MatchStatus
import com.sgale.gaztelubira.core.domain.model.match.MatchStatus.LOCAL
import com.sgale.gaztelubira.core.domain.model.match.MatchStatus.VISITOR
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.team.TeamSeason
import com.sgale.gaztelubira.core.domain.model.team.TeamStreak

internal class GazteluBiraHomeHandler(
    private val appTeam: TeamModel,
    private val matches: List<MatchModel>
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
        matches.sortedBy { it.date }.forEach { match ->
            val status = getStatus(match)
            updateMatchResult(status, match)
            updateMatchGoals(status, match)
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

    private fun getStatus(match: MatchModel): MatchStatus =
        when {
            match.localTeam.id == appTeam.id -> LOCAL
            else -> VISITOR
        }


    private fun getStreak() =
        TeamStreak(
            currentStreak = getCurrentStreak(),
            lastGames = streak.games.toList()
        )

    private fun provideGBInformation(): TeamSeason {
        val points = getPoints(results)

        return TeamSeason(
            id = appTeam.id,
            team = appTeam,
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
        status: MatchStatus,
        match: MatchModel
    ) {
        when (status) {
            LOCAL -> {
                goals.goalsFor += match.localGoals
                goals.goalsAgainst += match.visitorGoals
            }

            VISITOR -> {
                goals.goalsFor += match.visitorGoals
                goals.goalsAgainst += match.localGoals
            }
        }
    }

    private fun updateMatchResult(
        status: MatchStatus,
        match: MatchModel
    ) {
        if (match.localGoals == match.visitorGoals) {
            streak.games.add(DRAW)
            results.draws += 1
            return
        }

        var streakResult = VICTORY
        when (status) {
            LOCAL -> {
                if (match.localGoals > match.visitorGoals) {
                    results.wins += 1
                } else {
                    results.loses += 1
                    streakResult = DEFEAT
                }
            }

            VISITOR -> {
                if (match.localGoals > match.visitorGoals) {
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
