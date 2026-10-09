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

package com.sgale.gaztelubira.core.database.mapper

import com.sgale.gaztelubira.core.database.DatabaseMapper
import com.sgale.gaztelubira.core.database.entity.PlayerStatsEntity
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats

internal object PlayerStatsMapper : DatabaseMapper<PlayerStats, PlayerStatsEntity> {
    override fun PlayerStats.asEntity(): PlayerStatsEntity =
        PlayerStatsEntity(
            playerId = playerId,
            matchId = matchId,
            assists = assists,
            cleanSheets = cleanSheets,
            fails = fails,
            goals = goals,
            goalsProvoked = goalsProvoked,
            penaltiesProvoked = penaltiesProvoked,
            saves = saves,
            blueCards = blueCards,
            redCards = redCards,
            yellowCards = yellowCards
        )

    override fun PlayerStatsEntity.asModel(): PlayerStats =
        PlayerStats(
            playerId = playerId,
            matchId = matchId,
            assists = assists,
            cleanSheets = cleanSheets,
            fails = fails,
            goals = goals,
            goalsProvoked = goalsProvoked,
            penaltiesProvoked = penaltiesProvoked,
            saves = saves,
            blueCards = blueCards,
            redCards = redCards,
            yellowCards = yellowCards
        )
}
