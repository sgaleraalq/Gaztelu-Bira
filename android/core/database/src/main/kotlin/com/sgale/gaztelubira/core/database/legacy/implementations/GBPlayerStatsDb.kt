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

package com.sgale.gaztelubira.core.database.legacy.implementations

import com.sgale.gaztelubira.core.database.legacy.GBDatabase
import com.sgale.gaztelubira.core.database.legacy.entities.player.LegacyPlayerStatsEntity
import com.sgale.gaztelubira.core.domain.legacy.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.legacy.model.player.PlayerStats.Companion.ERROR_PLAYER_STATS
import com.sgale.gaztelubira.core.domain.legacy.model.stats.Stats
import com.sgale.gaztelubira.core.domain.legacy.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.legacy.repository.db.IGBPlayersStatsDb
import javax.inject.Inject

class GBPlayerStatsDb @Inject constructor(
    db: GBDatabase
) : AbstractGBDb(db), IGBPlayersStatsDb {

    private val playersStatsDao = db.getPlayersStatsDao()

    override suspend fun insertStats(
        matchId: FirebaseId,
        stats: Map<FirebaseId, Stats>
    ) {
        stats.forEach { (playerId, stats) ->
            val player = playersStatsDao.getItem(playerId)

            if (player == null) {
                val newPlayer = LegacyPlayerStatsEntity(
                    id = playerId,
                    stats = mapOf(matchId to stats)
                )
                playersStatsDao.insert(newPlayer)
            } else {
                // TODO
//                val playersMap = getPlayersMap()
//                val playerName = playersMap[player.id]?.name ?: "Unknown"
                val newPlayerStats = player.copy(
                    stats = player.stats + (matchId to stats)
                )
                playersStatsDao.insert(newPlayerStats)
            }
        }
    }

    private fun PlayerStats.asEntity() =
        LegacyPlayerStatsEntity(
            id = id,
            stats = stats
        )

    private fun LegacyPlayerStatsEntity.asModel() =
        ERROR_PLAYER_STATS // TODO
}
