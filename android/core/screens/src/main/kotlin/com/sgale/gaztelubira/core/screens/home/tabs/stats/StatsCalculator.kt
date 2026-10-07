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

package com.sgale.gaztelubira.core.screens.home.tabs.stats

import com.sgale.gaztelubira.core.domain.legacy.model.player.Position.MANAGER
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.migration.model.season.squad.SquadPlayer
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.ASSISTS
import com.sgale.gaztelubira.multiplatform.model.GBStat.BLUE_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.CLEAN_SHEETS
import com.sgale.gaztelubira.multiplatform.model.GBStat.FAILS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GAMES_PLAYED
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBStat.PENALTIES_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBStat.PERCENTAGE
import com.sgale.gaztelubira.multiplatform.model.GBStat.RED_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.SAVES
import com.sgale.gaztelubira.multiplatform.model.GBStat.YELLOW_CARDS

internal data class SeasonStats(
    val squad: List<SquadPlayer>,
    val stats: Map<PlayerId, List<PlayerStats>>,
    val lastMatchId: MatchId?
) {
    companion object {
        val EMPTY = SeasonStats(
            squad = emptyList(),
            stats = emptyMap(),
            lastMatchId = null
        )
    }
}

internal data class RankedPlayer(
    val player: SquadPlayer,
    val value: Double,
    val changedPosition: Int
)

internal object StatsCalculator {
    /**
     * [RankedPlayer.changedPosition] compares against the ranking before the season's last match.
     */
    fun rank(
        season: SeasonStats,
        stat: GBStat,
        punctuation: GBPunctuation
    ): List<RankedPlayer> {
        val current = season.ranking(stat, punctuation) { true }
        val previous = season.lastMatchId
            ?.let { last -> season.ranking(stat, punctuation) { it.matchId != last } }
            .orEmpty()
        val previousPositions = previous
            .mapIndexed { index, (player, _) -> player.player.id to index }
            .toMap()

        return current.mapIndexed { index, (player, value) ->
            val previousPosition = previousPositions[player.player.id] ?: index
            RankedPlayer(
                player = player,
                value = value,
                changedPosition = previousPosition - index
            )
        }
    }

    fun total(
        stats: List<PlayerStats>,
        stat: GBStat,
        punctuation: GBPunctuation
    ): Double =
        when (stat) {
            PERCENTAGE -> percentage(stats, punctuation)
            GOALS -> stats.sumOf { it.goals }.toDouble()
            GOALS_PROVOKED -> stats.sumOf { it.goalsProvoked }.toDouble()
            ASSISTS -> stats.sumOf { it.assists }.toDouble()
            CLEAN_SHEETS -> stats.sumOf { it.cleanSheets }
            PENALTIES_PROVOKED -> stats.sumOf { it.penaltiesProvoked }.toDouble()
            SAVES -> stats.sumOf { it.saves }.toDouble()
            FAILS -> stats.sumOf { it.fails }.toDouble()
            YELLOW_CARDS -> stats.sumOf { it.yellowCards }.toDouble()
            RED_CARDS -> stats.sumOf { it.redCards }.toDouble()
            BLUE_CARDS -> stats.sumOf { it.blueCards }.toDouble()
            GAMES_PLAYED -> stats.size.toDouble()
        }

    private fun SeasonStats.ranking(
        stat: GBStat,
        punctuation: GBPunctuation,
        counts: (PlayerStats) -> Boolean
    ): List<Pair<SquadPlayer, Double>> =
        squad
            .filter { it.position != MANAGER }
            .map { player ->
                val playerStats = stats[player.player.id].orEmpty().filter(counts)
                player to total(playerStats, stat, punctuation)
            }
            .sortedWith(
                compareByDescending<Pair<SquadPlayer, Double>> { it.second }
                    .thenBy { it.first.player.name }
            )

    private fun percentage(
        stats: List<PlayerStats>,
        punctuation: GBPunctuation
    ): Double {
        if (stats.isEmpty()) return 0.0

        val goals = punctuation.goals * stats.sumOf { it.goals + it.goalsProvoked }
        val assists = punctuation.assists * stats.sumOf { it.assists }
        val penalties = punctuation.penaltiesProvoked * stats.sumOf { it.penaltiesProvoked }
        val cleanSheets = punctuation.cleanSheets * stats.sumOf { it.cleanSheets }
        val saves = punctuation.saves * stats.sumOf { it.saves }
        val redCards = -(stats.sumOf { it.redCards } / punctuation.minRedCards)
        val yellowCards = -(stats.sumOf { it.yellowCards } / punctuation.minYellowCards)
        val fails = punctuation.fails * stats.sumOf { it.fails }

        val points = goals + assists + penalties + cleanSheets + saves + redCards + yellowCards + fails
        return points / stats.size
    }
}
