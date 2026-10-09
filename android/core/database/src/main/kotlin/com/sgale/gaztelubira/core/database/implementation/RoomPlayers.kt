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

package com.sgale.gaztelubira.core.database.implementation

import com.sgale.gaztelubira.core.database.dao.PlayerDao
import com.sgale.gaztelubira.core.database.mapper.PlayerMapper.asEntity
import com.sgale.gaztelubira.core.database.mapper.PlayerMapper.asModel
import com.sgale.gaztelubira.core.database.mapper.PlayerStatsMapper.asEntity
import com.sgale.gaztelubira.core.database.mapper.PlayerStatsMapper.asModel
import com.sgale.gaztelubira.core.domain.model.match.MatchId
import com.sgale.gaztelubira.core.domain.model.player.Player
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.player.PlayerStats
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.repository.PlayerLocal
import javax.inject.Inject

internal class RoomPlayers @Inject constructor(
    private val playerDao: PlayerDao
) : PlayerLocal {

    override suspend fun getPlayer(id: PlayerId): Player =
        playerDao.getPlayer(id).asModel()

    override suspend fun getPlayers(): List<Player> =
        playerDao.getPlayers().map { it.asModel() }

    override suspend fun getMatchPlayerStats(matchId: MatchId): List<PlayerStats> =
        playerDao.getMatchPlayerStats(matchId).map { it.asModel() }

    override suspend fun getSeasonPlayerStats(seasonId: SeasonId): List<PlayerStats> =
        playerDao.getSeasonPlayerStats(seasonId).map { it.asModel() }

    override suspend fun getSeasonPlayerStats(playerId: PlayerId, seasonId: SeasonId): List<PlayerStats> =
        playerDao.getSeasonPlayerStats(playerId, seasonId).map { it.asModel() }

    override suspend fun insertPlayer(player: Player) =
        playerDao.insertPlayer(player.asEntity())

    override suspend fun insertPlayers(players: List<Player>) =
        playerDao.insertPlayers(players.map { it.asEntity() })

    override suspend fun insertMatchStats(matchId: MatchId, stats: List<PlayerStats>) =
        playerDao.replaceMatchStats(matchId, stats.map { it.asEntity().copy(matchId = matchId) })
}
