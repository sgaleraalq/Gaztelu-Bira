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

package com.sgale.gaztelubira.core.screens.mapper

import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.utils.formatDecimal
import com.sgale.gaztelubira.core.screens.home.tabs.stats.RankedPlayer
import com.sgale.gaztelubira.core.screens.home.tabs.stats.SeasonStats
import com.sgale.gaztelubira.core.screens.home.tabs.stats.StatsCalculator
import com.sgale.gaztelubira.core.screens.mapper.PlayerUiMapper.displayName
import com.sgale.gaztelubira.core.screens.mapper.PlayerUiMapper.toGBPosition
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStat
import com.sgale.gaztelubira.multiplatform.model.GBPlayerStatsDetail
import com.sgale.gaztelubira.multiplatform.model.GBPunctuation
import com.sgale.gaztelubira.multiplatform.model.GBStat
import com.sgale.gaztelubira.multiplatform.model.GBStat.ASSISTS
import com.sgale.gaztelubira.multiplatform.model.GBStat.BLUE_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.CLEAN_SHEETS
import com.sgale.gaztelubira.multiplatform.model.GBStat.FAILS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS
import com.sgale.gaztelubira.multiplatform.model.GBStat.GOALS_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBStat.PENALTIES_PROVOKED
import com.sgale.gaztelubira.multiplatform.model.GBStat.PERCENTAGE
import com.sgale.gaztelubira.multiplatform.model.GBStat.RED_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStat.SAVES
import com.sgale.gaztelubira.multiplatform.model.GBStat.YELLOW_CARDS
import com.sgale.gaztelubira.multiplatform.model.GBStatValue

internal object StatsUiMapper {
    fun Stat.toGBStat(): GBStat = when (this) {
        Stat.ASSIST -> ASSISTS
        Stat.CLEAN_SHEET -> CLEAN_SHEETS
        Stat.FAILS -> FAILS
        Stat.GOALS -> GOALS
        Stat.GOALS_PROVOKED -> GOALS_PROVOKED
        Stat.PENALTIES_PROVOKED -> PENALTIES_PROVOKED
        Stat.SAVES -> SAVES
        Stat.BLUE_CARDS -> BLUE_CARDS
        Stat.RED_CARDS -> RED_CARDS
        Stat.YELLOW_CARDS -> YELLOW_CARDS
    }

    fun RankedPlayer.toGBPlayerStat(): GBPlayerStat =
        GBPlayerStat(
            id = player.player.id.value,
            name = player.player.displayName(),
            dorsal = player.dorsal,
            faceImage = player.player.faceImage,
            bodyImage = player.player.bodyImage,
            position = player.position.toGBPosition(),
            value = formatDecimal(value),
            changedPosition = changedPosition
        )

    fun SeasonStats.toDetail(
        id: PlayerId,
        punctuation: GBPunctuation
    ): GBPlayerStatsDetail? {
        val player = squad.find { it.player.id == id }?.player ?: return null
        val playerStats = stats[id].orEmpty()

        return GBPlayerStatsDetail(
            name = player.displayName(),
            faceImage = player.faceImage,
            stats = GBStat.entries.filter { it != BLUE_CARDS }.map { stat ->
                val value = formatDecimal(StatsCalculator.total(playerStats, stat, punctuation))
                GBStatValue(stat, if (stat == PERCENTAGE) "$value %" else value)
            }
        )
    }
}
