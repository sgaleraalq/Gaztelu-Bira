/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

import com.sgale.gaztelubira.core.domain.model.utils.GazteluBiraUtils.TESTING
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.match.MatchResult
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.Defeat
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.Draw
import com.sgale.gaztelubira.core.domain.model.match.MatchResult.Victory
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.GBHomeHelper.provideGBInformation
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.GazteluBiraHomeHandler.MatchStatus.Local
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.GazteluBiraHomeHandler.MatchStatus.Undefined
import com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira.GazteluBiraHomeHandler.MatchStatus.Visitor

class GazteluBiraHomeHandler(
    private val appTeam: TeamModel,
    private val matches: List<MatchModel>
) {
    enum class MatchStatus { Local, Visitor, Undefined }

    data class MatchResults(
        var wins: Int = 0,
        var draws: Int = 0,
        var loses: Int = 0
    )

    data class MatchGoals(
        var goalsFor: Int = 0,
        var goalsAgainst: Int = 0
    )

    data class MatchStreak(
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
            if (result == Victory) {
                count++
            } else {
                break
            }
        }
        return count
    }

    fun getGBInformation(): GBInformation {
        return if (TESTING){
            provideGBInformation(appTeam)
        } else {
            provideGBInformation()
        }
    }

    private fun getPoints(results: MatchResults): Int =
        (results.wins * 3) + results.draws

    private fun getStatus(match: MatchModel): MatchStatus {
        return if (match.localTeam.id == appTeam.id) {
            Local
        } else if (match.visitorTeam.id == appTeam.id) {
            Visitor
        } else {
            Undefined
        }
    }

    private fun getStreak(): Streak {
        return Streak(
            currentStreak = getCurrentStreak(),
            lastGames = streak.games.toList()
        )
    }

    private fun provideGBInformation(): GBInformation {
        val points = getPoints(results)

        return GBInformation(
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
        when(status) {
            Local -> {
                goals.goalsFor += match.localGoals
                goals.goalsAgainst += match.visitorGoals
            }
            Visitor -> {
                goals.goalsFor += match.visitorGoals
                goals.goalsAgainst += match.localGoals
            }
            Undefined -> { }
        }
    }

    private fun updateMatchResult(
        status: MatchStatus,
        match: MatchModel
    ) {
        if (status == Undefined) return

        if (match.localGoals == match.visitorGoals) {
            streak.games.add(Draw)
            results.draws += 1
            return
        }

        var streakResult = Victory
        when (status) {
            Local -> {
                if (match.localGoals > match.visitorGoals) {
                    results.wins += 1
                } else {
                    results.loses += 1
                    streakResult = Defeat
                }
            }
            Visitor -> {
                if (match.localGoals > match.visitorGoals) {
                    results.loses += 1
                    streakResult = Defeat
                } else {
                    results.wins += 1
                }
            }
            else -> {}
        }

        streak.games.add(streakResult)
    }
}
