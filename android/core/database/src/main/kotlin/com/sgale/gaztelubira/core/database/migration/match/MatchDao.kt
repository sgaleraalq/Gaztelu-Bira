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

package com.sgale.gaztelubira.core.database.migration.match

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sgale.gaztelubira.core.database.migration.match.entity.MatchEntity
import com.sgale.gaztelubira.core.database.migration.match.entity.MatchEventEntity
import com.sgale.gaztelubira.core.database.migration.match.entity.MatchPlayerEntity
import com.sgale.gaztelubira.core.domain.migration.model.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId

@Dao
internal interface MatchDao {

    @Query("SELECT * FROM MatchEntity WHERE id = :matchId")
    suspend fun getMatch(matchId: MatchId): MatchEntity

    @Query("SELECT * FROM MatchEntity WHERE seasonId = :seasonId")
    suspend fun getMatches(seasonId: SeasonId): List<MatchEntity>

    @Upsert
    suspend fun insertMatch(entity: MatchEntity)

    @Upsert
    suspend fun insertMatches(entity: List<MatchEntity>)

    /**
     * The match's events are swapped as a whole, so an event removed remotely does not linger here.
     */
    @Transaction
    suspend fun replaceMatchEvents(matchId: MatchId, events: List<MatchEventEntity>) {
        deleteMatchEvents(matchId)
        insertMatchEvents(events)
    }

    @Query("DELETE FROM MatchEventEntity WHERE matchId = :matchId")
    suspend fun deleteMatchEvents(matchId: MatchId)

    @Insert
    suspend fun insertMatchEvents(events: List<MatchEventEntity>)

    /**
     * The match's players are swapped as a whole, so a player removed remotely does not linger here.
     */
    @Transaction
    suspend fun replaceMatchPlayers(matchId: MatchId, players: List<MatchPlayerEntity>) {
        deleteMatchPlayers(matchId)
        insertMatchPlayers(players)
    }

    @Query("DELETE FROM MatchPlayerEntity WHERE matchId = :matchId")
    suspend fun deleteMatchPlayers(matchId: MatchId)

    @Insert
    suspend fun insertMatchPlayers(players: List<MatchPlayerEntity>)
}
