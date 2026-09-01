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

package com.sgale.gaztelubira.core.screens.insert_match.data

import androidx.compose.runtime.Stable
import com.sgale.gaztelubira.core.domain.model.match.MatchStats
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.player.Stat
import com.sgale.gaztelubira.core.domain.model.player.Stat.Goals

@Stable
data class InsertMatchStats(
    val selectedStat: Stat? = Goals,
    val assists: List<PlayerModel> = emptyList(),
    val cleanSheets: List<PlayerModel> = emptyList(),
    val fails: List<PlayerModel> = emptyList(),
    val goals: List<PlayerModel> = emptyList(),
    val goalsProvoked: List<PlayerModel> = emptyList(),
    val penaltiesProvoked: List<PlayerModel> = emptyList(),
    val redCards: List<PlayerModel> = emptyList(),
    val saves: List<PlayerModel> = emptyList(),
    val yellowCards: List<PlayerModel> = emptyList()
) {
    fun toStatsMatchModel() = MatchStats(
        assists = assists,
        cleanSheets = cleanSheets,
        fails = fails,
        goals = goals,
        goalsProvoked = goalsProvoked,
        penaltiesProvoked = penaltiesProvoked,
        redCards = redCards,
        saves = saves,
        yellowCards = yellowCards
    )
}
