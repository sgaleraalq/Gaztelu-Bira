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

package com.sgale.gaztelubira.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.sgale.gaztelubira.core.database.entity.PlayerEntity
import com.sgale.gaztelubira.core.database.entity.PlayerStatsEntity
import com.sgale.gaztelubira.core.domain.migration.model.match.MatchId
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId

@Dao
internal interface PlayerDao {
    @Query("SELECT * FROM PlayerEntity WHERE id = :id")
    suspend fun getPlayer(id: PlayerId): PlayerEntity

    @Query("SELECT * FROM PlayerEntity")
    suspend fun getPlayers(): List<PlayerEntity>

    @Query("SELECT * FROM PlayerStatsEntity WHERE matchId = :matchId")
    suspend fun getMatchPlayerStats(matchId: MatchId): List<PlayerStatsEntity>

    @Query(
        """
        SELECT PlayerStatsEntity.* FROM PlayerStatsEntity
        INNER JOIN MatchEntity ON MatchEntity.id = PlayerStatsEntity.matchId
        WHERE MatchEntity.seasonId = :seasonId
        """
    )
    suspend fun getSeasonPlayerStats(seasonId: SeasonId): List<PlayerStatsEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertPlayer(entity: PlayerEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertPlayers(entity: List<PlayerEntity>)

    @Transaction
    suspend fun replaceMatchStats(matchId: MatchId, stats: List<PlayerStatsEntity>) {
        deleteMatchStats(matchId)
        insertMatchStats(stats)
    }

    @Query("DELETE FROM PlayerStatsEntity WHERE matchId = :matchId")
    suspend fun deleteMatchStats(matchId: MatchId)

    @Insert
    suspend fun insertMatchStats(stats: List<PlayerStatsEntity>)
}
