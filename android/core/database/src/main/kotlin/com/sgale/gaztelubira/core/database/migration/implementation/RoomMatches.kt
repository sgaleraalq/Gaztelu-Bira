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

package com.sgale.gaztelubira.core.database.migration.implementation

import com.sgale.gaztelubira.core.database.migration.dao.MatchDao
import com.sgale.gaztelubira.core.database.migration.mapper.MatchEventMapper.asEntity
import com.sgale.gaztelubira.core.database.migration.mapper.MatchEventMapper.asModel
import com.sgale.gaztelubira.core.database.migration.mapper.MatchLineupPlayerMapper.asEntity
import com.sgale.gaztelubira.core.database.migration.mapper.MatchLineupPlayerMapper.asModel
import com.sgale.gaztelubira.core.database.migration.mapper.MatchMapper.asEntity
import com.sgale.gaztelubira.core.database.migration.mapper.MatchMapper.asModel
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.match.Match
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchEvent
import com.sgale.gaztelubira.core.domain.migration.model.match.detail.MatchLineupPlayer
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId
import com.sgale.gaztelubira.core.domain.migration.repository.MatchLocal
import javax.inject.Inject

internal class RoomMatches @Inject constructor(
    private val matchDao: MatchDao
): MatchLocal {
    override suspend fun getMatch(matchId: MatchId): Match =
        matchDao.getMatch(matchId).asModel()

    override suspend fun getMatches(seasonId: SeasonId): List<Match> =
        matchDao.getMatches(seasonId).map { it.asModel() }

    override suspend fun getMatchEvents(matchId: MatchId): List<MatchEvent> =
        matchDao.getMatchEvents(matchId).map { it.asModel() }

    override suspend fun getMatchLineupPlayer(matchId: MatchId): List<MatchLineupPlayer> =
        matchDao.getMatchPlayers(matchId).map { it.asModel() }

    override suspend fun insertMatches(matches: List<Match>) {
        matchDao.insertMatches(matches.map { it.asEntity() })
    }

    override suspend fun insertMatchEvents(matchId: MatchId, events: List<MatchEvent>) {
        matchDao.replaceMatchEvents(matchId, events.map { it.asEntity().copy(matchId = matchId) })
    }

    override suspend fun insertMatchPlayers(matchId: MatchId, players: List<MatchLineupPlayer>) {
        matchDao.replaceMatchPlayers(matchId, players.map { it.asEntity().copy(matchId = matchId) })
    }
}
