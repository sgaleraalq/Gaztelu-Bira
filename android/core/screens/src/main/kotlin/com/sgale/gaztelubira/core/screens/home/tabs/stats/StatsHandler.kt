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

package com.sgale.gaztelubira.core.screens.home.tabs.stats

import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Position.Manager
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.player.Stat.Assists
import com.sgale.gaztelubira.core.domain.model.player.Stat.CleanSheets
import com.sgale.gaztelubira.core.domain.model.player.Stat.Fails
import com.sgale.gaztelubira.core.domain.model.player.Stat.GamesPlayed
import com.sgale.gaztelubira.core.domain.model.player.Stat.Goals
import com.sgale.gaztelubira.core.domain.model.player.Stat.GoalsProvoked
import com.sgale.gaztelubira.core.domain.model.player.Stat.PenaltiesProvoked
import com.sgale.gaztelubira.core.domain.model.player.Stat.Percentage
import com.sgale.gaztelubira.core.domain.model.player.Stat.RedCards
import com.sgale.gaztelubira.core.domain.model.player.Stat.Saves
import com.sgale.gaztelubira.core.domain.model.player.Stat.YellowCards
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.PlayerDisplayStats
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StatsHandler(
    private val selectedStat: StateFlow<Stat>,
    private val playersList: Flow<List<PlayerStatsModel>>,
    private val matchesFlow: Flow<List<MatchModel>>,
    scope: CoroutineScope
) {
    private val punctuation = MutableStateFlow(Punctuation())
    private val _statsDisplayed = MutableStateFlow<List<PlayerDisplayStats>>(emptyList())
    val statsDisplayed: StateFlow<List<PlayerDisplayStats>> = _statsDisplayed
    val valueChanged = MutableStateFlow(false)

    init {
        scope.launch {
            combine(
                playersList,
                selectedStat,
                punctuation,
                matchesFlow
            ) { players, stat, punctuation, matches ->
                valueChanged.value = true
                val prevPositions = calculatePrevPositions(
                    selectedStat = stat,
                    players = players,
                    punctuation = punctuation,
                    matches = matches
                )

                val currentRanking = players
                    .filter { it.player.position != Manager }
                    .map { player ->
                        val statValue = calculateStats(player, stat, punctuation)
                        PlayerDisplayStats(
                            id = player.id,
                            player = player.player,
                            stat = statValue,
                            changedPosition = 0
                        )
                    }
                    .sortedWith(
                        compareByDescending<PlayerDisplayStats> { it.stat }
                            .thenBy { it.player.name }
                    )

                val prevPositionsMap: Map<String, Int> =
                    prevPositions
                        .mapIndexed { index, p -> p.id to (index + 1) }
                        .toMap()

                currentRanking.mapIndexed { index, playerDisplay ->
                    val currentPos = index + 1
                    val prevPos = prevPositionsMap[playerDisplay.id] ?: currentPos
                    val changed = prevPos - currentPos

                    playerDisplay.copy(changedPosition = changed)
                }
            }.collectLatest { computedList ->
                _statsDisplayed.value = computedList
                valueChanged.value = false
            }
        }
    }

    fun calculatePercentage(
        player: PlayerStatsModel,
        punctuation: Punctuation
    ): Double {
        val tGamesPlayed = player.stats.values.sumOf { it.gamesPlayed }
        if (tGamesPlayed == 0) return 0.0

        val tGoals = player.stats.values.sumOf { it.goals }
        val tGoalsProvoked = player.stats.values.sumOf { it.goalsProvoked }
        val tAssists = player.stats.values.sumOf { it.assists }
        val tPenalties = player.stats.values.sumOf { it.penaltiesProvoked }
        val tCleanSheets = player.stats.values.sumOf { it.cleanSheets }
        val tSaves = player.stats.values.sumOf { it.saves }
        val tRedCards = player.stats.values.sumOf { it.redCards }
        val tYellowCards = player.stats.values.sumOf { it.yellowCards }
        val tFails = player.stats.values.sumOf { it.fails }

        val pGoals = punctuation.goals * (tGoals + tGoalsProvoked)
        val pAssists = punctuation.assists * tAssists
        val pPenalties = punctuation.penaltiesProvoked * tPenalties
        val pCleanSheets = punctuation.cleanSheets * tCleanSheets
        val pSaves = punctuation.saves * tSaves
        val pRedCard = (tRedCards / punctuation.minRedCards) * (-1)
        val pYellowCard = (tYellowCards / punctuation.minYellowCards) * (-1)
        val pFails = punctuation.fails * tFails

        val totalPoints =
            pGoals + pAssists + pPenalties + pCleanSheets + pSaves + pRedCard + pYellowCard + pFails

        return totalPoints.toDouble() / tGamesPlayed.toDouble()
    }

    private fun calculatePrevPositions(
        selectedStat: Stat,
        players: List<PlayerStatsModel>,
        matches: List<MatchModel>,
        punctuation: Punctuation
    ): List<PlayerDisplayStats> {
        val lastMatchId = matches.lastOrNull()?.id ?: return emptyList()
        return players
            .filter { it.player.position != Manager }
            .map { player ->
                val prevStatsMap = player.stats.filterKeys { key -> key != lastMatchId }
                val prevPlayer = player.copy(stats = prevStatsMap)
                val statValue = calculateStats(prevPlayer, selectedStat, punctuation)
                PlayerDisplayStats(
                    id = player.id,
                    player = player.player,
                    stat = statValue,
                    changedPosition = 0
                )
            }
            .sortedWith(
                compareByDescending<PlayerDisplayStats> { it.stat }
                    .thenBy { it.player.name }
            )
    }

    private fun calculateStats(
        player: PlayerStatsModel,
        stat: Stat,
        punctuation: Punctuation
    ): Double {
        return when (stat) {
            Goals -> player.stats.values.sumOf { it.goals }.toDouble()
            GoalsProvoked -> player.stats.values.sumOf { it.goalsProvoked }.toDouble()
            Assists -> player.stats.values.sumOf { it.assists }.toDouble()
            PenaltiesProvoked -> player.stats.values.sumOf { it.penaltiesProvoked }.toDouble()
            CleanSheets -> player.stats.values.sumOf { it.cleanSheets }.toDouble()
            Saves -> player.stats.values.sumOf { it.saves }.toDouble()
            RedCards -> player.stats.values.sumOf { it.redCards }.toDouble()
            YellowCards -> player.stats.values.sumOf { it.yellowCards }.toDouble()
            GamesPlayed -> player.stats.values.sumOf { it.gamesPlayed }.toDouble()
            Fails -> player.stats.values.sumOf { it.fails }.toDouble()
            Percentage -> calculatePercentage(player, punctuation)
        }
    }

    fun changePunctuation(newPunctuation: Punctuation) {
        punctuation.value = newPunctuation
    }
}
