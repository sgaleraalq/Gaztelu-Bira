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

import com.sgale.gaztelubira.core.database.dao.SeasonDao
import com.sgale.gaztelubira.core.database.mapper.SeasonMapper.asEntity
import com.sgale.gaztelubira.core.database.mapper.SeasonMapper.asModel
import com.sgale.gaztelubira.core.database.mapper.SeasonPlayerMapper.asEntity
import com.sgale.gaztelubira.core.database.mapper.SeasonPlayerMapper.asModel
import com.sgale.gaztelubira.core.database.other.SquadPlayerMapper.asModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.model.season.Season
import com.sgale.gaztelubira.core.domain.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.model.season.squad.SeasonPlayer
import com.sgale.gaztelubira.core.domain.model.season.squad.SquadPlayer
import com.sgale.gaztelubira.core.domain.repository.SeasonLocal
import javax.inject.Inject

internal class RoomSeasons @Inject constructor(
    private val seasonDao: SeasonDao
): SeasonLocal {
    override suspend fun getSeasons(): List<Season> =
        seasonDao.getSeasons().map { it.asModel() }

    override suspend fun getSeasonPlayer(playerId: PlayerId, seasonId: SeasonId): SeasonPlayer =
        seasonDao.getSeasonPlayer(playerId, seasonId).asModel()

    override suspend fun getSeasonPlayers(season: SeasonId): List<SeasonPlayer> =
        seasonDao.getSeasonPlayers(season).map { it.asModel() }

    override suspend fun getSquad(season: SeasonId): List<SquadPlayer> =
        seasonDao.getSquad(season).map { it.asModel() }

    override suspend fun insertSeasons(seasons: List<Season>) =
        seasonDao.insertSeasons(seasons.map { it.asEntity() })

    override suspend fun insertSeasonPlayers(players: List<SeasonPlayer>) =
        seasonDao.insertSeasonPlayers(players.map { it.asEntity() })
}
