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

package com.sgale.gaztelubira.core.data.db.implementations

import com.sgale.gaztelubira.core.data.db.GBDatabase
import com.sgale.gaztelubira.core.data.db.entities.PlayerStatsEntity
import com.sgale.gaztelubira.core.data.mappers.PlayerStatsMapper.asEntity
import com.sgale.gaztelubira.core.data.mappers.PlayerStatsMapper.asModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.model.stats.Stats
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersStatsDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GBPlayerStatsDb @Inject constructor(
    db: GBDatabase
) : AbstractGBDb(db), IGBPlayersStatsDb {

    private val playersStatsDao = db.getPlayersStatsDao()

    override suspend fun deletePlayer(id: FirebaseId) {
        playersStatsDao.deleteItem(id)
    }

    override suspend fun getPlayerStats(id: FirebaseId): PlayerStats? =
        playersStatsDao.getItem(id)?.asModel()

    override suspend fun insertPlayer(player: PlayerStats) {
        playersStatsDao.insert(player.asEntity())
    }

    override suspend fun insertStats(
        matchId: FirebaseId,
        stats: Map<FirebaseId, Stats>
    ) {
        stats.forEach { (playerId, stats) ->
            val player = playersStatsDao.getItem(playerId)

            if (player == null) {
                val newPlayer = PlayerStatsEntity(
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

    override suspend fun insertStatsFromFB(
        stats: List<PlayerStats>
    ) {
        insertList(
            items = stats,
            mapper = { it.asEntity() },
            dao = playersStatsDao
        )
    }

    override fun getPlayersStatsListAsFlow(): Flow<List<PlayerStats>> =
        getFlow(
            source = playersStatsDao.getListAsFlow(),
            mapper = { it.asModel() },
            keySelector = { it.percentage }
        )
}
