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
import com.sgale.gaztelubira.core.data.mappers.PlayerMapper
import com.sgale.gaztelubira.core.data.mappers.asPlayerDomain
import com.sgale.gaztelubira.core.data.mappers.asPlayerEntity
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersDb
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GBPlayersDb @Inject constructor(
    db: GBDatabase
): AbstractGBDb(db), IGBPlayersDb {
    private val playersDao = db.getPlayersDao()

    override suspend fun deletePlayer(id: FirebaseId) {
        playersDao.deleteItem(id)
    }

    override suspend fun insertPlayer(player: PlayerModel) =
        playersDao.insert(player.asPlayerEntity())

    override suspend fun insertPlayers(players: List<PlayerModel>) =
        insertList(
            items = players,
            mapper = PlayerMapper::asEntity,
            dao = playersDao
        )

    override suspend fun getPlayer(id: FirebaseId): PlayerModel? =
        playersDao.getItem(id)?.asPlayerDomain()

    override suspend fun getNumberOfPlayers(): Int =
        playersDao.getDorsals().size

    override suspend fun getAvailableDorsals(): List<Int> {
        val taken = playersDao.getDorsals()
        return (1..100).filterNot { it in taken }
    }

    override suspend fun getPlayers(): List<PlayerModel> =
        playersDao.getPlayers().map { it.asPlayerDomain() }

    override fun getPlayersListAsFlow(): Flow<List<PlayerModel>> =
        getFlow(
            source = playersDao.getListAsFlow(),
            mapper = { it.asPlayerDomain() },
            keySelector = { it.id }
        )
}
