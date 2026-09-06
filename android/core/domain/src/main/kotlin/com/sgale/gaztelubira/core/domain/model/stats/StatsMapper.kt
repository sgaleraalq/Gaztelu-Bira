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

package com.sgale.gaztelubira.core.domain.model.stats

import com.sgale.gaztelubira.core.domain.model.player.PlayerDisplayStats
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.model.player.Position.Bench
import com.sgale.gaztelubira.core.domain.model.player.Position.Defender
import com.sgale.gaztelubira.core.domain.model.player.Position.Forward
import com.sgale.gaztelubira.core.domain.model.player.Position.GoalKeeper
import com.sgale.gaztelubira.core.domain.model.player.Position.Manager
import com.sgale.gaztelubira.core.domain.model.player.Position.MidFielder
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.utils.formatDecimal
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStat
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStatsDetail
import com.sgale.gaztelubira.multiplatform.model.GBPosition
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.ASSISTS
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
import com.sgale.gaztelubira.multiplatform.model.GBStatValue

object StatsMapper {
    fun PlayerDisplayStats.toGBPlayerStat(
        selectedStat: GBStat
    ) = GBPlayerStat(
        id = id,
        name = player.name,
        dorsal = player.dorsal,
        faceImage = player.faceImage,
        bodyImage = player.bodyImage,
        position = player.position?.toGBPosition(),
        value = displayValue(selectedStat, stat),
        changedPosition = changedPosition
    )

    fun PlayerStatsModel.toDetail(
        percentage: Double
    ) = GBPlayerStatsDetail(
        name = player.name,
        faceImage = player.faceImage,
        stats = GBStat.entries.map { gbStat ->
            val value = when (gbStat) {
                PERCENTAGE -> "${formatDecimal(percentage)} %"
                else -> total(gbStat).toString()
            }
            GBStatValue(gbStat, value)
        }
    )

    private fun PlayerStatsModel.total(gbStat: GBStat): Int =
        when (gbStat) {
            GOALS -> stats.values.sumOf { it.goals }
            GOALS_PROVOKED -> stats.values.sumOf { it.goalsProvoked }
            ASSISTS -> stats.values.sumOf { it.assists }
            CLEAN_SHEETS -> stats.values.sumOf { it.cleanSheets }
            PENALTIES_PROVOKED -> stats.values.sumOf { it.penaltiesProvoked }
            SAVES -> stats.values.sumOf { it.saves }
            FAILS -> stats.values.sumOf { it.fails }
            YELLOW_CARDS -> stats.values.sumOf { it.yellowCards }
            RED_CARDS -> stats.values.sumOf { it.redCards }
            GAMES_PLAYED -> stats.values.sumOf { it.gamesPlayed }
            PERCENTAGE -> 0
        }

    private fun displayValue(stat: GBStat, value: Double): String =
        when (stat) {
            PERCENTAGE -> formatDecimal(value)
            else -> value.toInt().toString()
        }

    fun GBStat.toStat(): Stat =
        when (this) {
            PERCENTAGE -> Stat.Percentage
            GOALS -> Stat.Goals
            GOALS_PROVOKED -> Stat.GoalsProvoked
            ASSISTS -> Stat.Assists
            CLEAN_SHEETS -> Stat.CleanSheets
            PENALTIES_PROVOKED -> Stat.PenaltiesProvoked
            SAVES -> Stat.Saves
            FAILS -> Stat.Fails
            YELLOW_CARDS -> Stat.YellowCards
            RED_CARDS -> Stat.RedCards
            GAMES_PLAYED -> Stat.GamesPlayed
        }

    private fun Position.toGBPosition(): GBPosition =
        when (this) {
            Manager -> GBPosition.MANAGER
            Bench -> GBPosition.BENCH
            GoalKeeper -> GBPosition.GOALKEEPER
            Defender -> GBPosition.DEFENDER
            MidFielder -> GBPosition.MID_FIELDER
            Forward -> GBPosition.FORWARD
        }
}
