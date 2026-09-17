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

package com.sgale.gaztelubira.core.network.firebase.response.stats

import com.sgale.gaztelubira.core.domain.model.stats.Stats
import com.sgale.gaztelubira.core.network.NetworkMapper
import com.sgale.gaztelubira.core.network.firebase.response.player.PlayerStatsResponse

// BIG TODO
internal object StatsMapper: NetworkMapper<Stats, PlayerStatsResponse> {
    override fun Stats.asResponse() =
        PlayerStatsResponse(
            assists = assists,
            cleanSheets = cleanSheets,
            fails = fails,
            gamesPlayed = gamesPlayed,
            goals = goals,
            goalsProvoked = goalsProvoked,
            penaltiesProvoked = penaltiesProvoked,
            redCards = redCards,
            saves = saves,
            yellowCards = yellowCards
        )

    override fun PlayerStatsResponse.asModel() =
        Stats(
            assists = assists,
            cleanSheets = cleanSheets,
            fails = fails,
            gamesPlayed = gamesPlayed,
            goals = goals,
            goalsProvoked = goalsProvoked,
            penaltiesProvoked = penaltiesProvoked,
            redCards = redCards,
            saves = saves,
            yellowCards = yellowCards
        )
}
