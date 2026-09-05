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
import com.sgale.gaztelubira.core.data.mappers.asPlayerStatsEntity
import com.sgale.gaztelubira.core.data.mappers.asPlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerStatsModel
import com.sgale.gaztelubira.core.domain.model.player.Stats
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersStatsDb
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GBPlayerStatsDb @Inject constructor(
    db: GBDatabase
) : AbstractGBDb(db), IGBPlayersStatsDb {

    private val playersStatsDao = db.getPlayersStatsDao()

    override suspend fun deletePlayer(id: FirebaseId) {
        playersStatsDao.deleteItem(id)
    }

    override suspend fun getPlayerStats(id: FirebaseId): PlayerStatsModel? =
        playersStatsDao.getItem(id)?.asPlayerStatsModel(getPlayersMap())

    override suspend fun insertPlayer(player: PlayerStatsModel) {
        playersStatsDao.insert(player.asPlayerStatsEntity())
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
                val playersMap = getPlayersMap()
                val playerName = playersMap[player.id]?.name ?: "Unknown"
                println("Inserting player $playerName")
                val newPlayerStats = player.copy(
                    stats = player.stats + (matchId to stats)
                )
                playersStatsDao.insert(newPlayerStats)
            }
        }
    }

    override suspend fun insertStatsFromFB(
        stats: List<PlayerStatsModel>
    ) {
        insertList(
            items = stats,
            mapper = { it.asPlayerStatsEntity() },
            dao = playersStatsDao
        )
    }

    override fun getPlayersStatsListAsFlow(): Flow<List<PlayerStatsModel>> =
        getFlow(
            source = playersStatsDao.getListAsFlow(),
            mapper = { it.asPlayerStatsModel(getPlayersMap()) },
            keySelector = { it.percentage }
        )
}
