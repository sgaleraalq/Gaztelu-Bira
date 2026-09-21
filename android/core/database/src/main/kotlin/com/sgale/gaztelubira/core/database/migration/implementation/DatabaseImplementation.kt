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

package com.sgale.gaztelubira.core.database.migration.implementation

import com.sgale.gaztelubira.core.database.migration.dao.PlayerDao
import com.sgale.gaztelubira.core.database.migration.entity.player.PlayerMapper.asEntity
import com.sgale.gaztelubira.core.database.migration.entity.player.PlayerMapper.asModel
import com.sgale.gaztelubira.core.domain.migration.model.player.Player
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.repository.database.GBDatabase
import javax.inject.Inject

internal class DatabaseImplementation @Inject constructor(
    private val playerDao: PlayerDao
) : GBDatabase {
    override suspend fun getPlayer(player: PlayerId): Player =
        playerDao.getPlayer(player).asModel()

    override suspend fun getPlayers(): List<Player> =
        playerDao.getPlayers().map { it.asModel() }

    override suspend fun insertPlayer(player: Player) =
        playerDao.insertPlayer(player.asEntity())

    override suspend fun insertPlayers(players: List<Player>) =
        playerDao.insertPlayers(players.map { it.asEntity() })
}
