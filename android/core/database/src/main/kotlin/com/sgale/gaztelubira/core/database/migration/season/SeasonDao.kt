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

package com.sgale.gaztelubira.core.database.migration.season

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.sgale.gaztelubira.core.database.migration.season.entity.SeasonPlayerEntity
import com.sgale.gaztelubira.core.database.migration.season.entity.SquadPlayerView
import com.sgale.gaztelubira.core.domain.migration.model.player.PlayerId
import com.sgale.gaztelubira.core.domain.migration.model.season.SeasonId

@Dao
internal interface SeasonDao {
    @Query("SELECT * FROM SeasonEntity")
    suspend fun getSeasons(): List<SeasonEntity>

    @Query("SELECT * FROM SeasonPlayerEntity WHERE seasonId = :seasonId AND playerId = :playerId")
    suspend fun getSeasonPlayer(playerId: PlayerId, seasonId: SeasonId): SeasonPlayerEntity

    @Query("SELECT * FROM SeasonPlayerEntity WHERE seasonId = :seasonId ORDER BY dorsal IS NULL, dorsal")
    suspend fun getSeasonPlayers(seasonId: SeasonId): List<SeasonPlayerEntity>

    @Query(
        """
        SELECT p.*, s.dorsal, s.position
        FROM SeasonPlayerEntity s
        INNER JOIN PlayerEntity p ON p.id = s.playerId
        WHERE s.seasonId = :seasonId
        ORDER BY s.dorsal IS NULL, s.dorsal
        """
    )
    suspend fun getSquad(seasonId: SeasonId): List<SquadPlayerView>

    @Insert(onConflict = REPLACE)
    suspend fun insertSeasons(entity: List<SeasonEntity>)

    @Insert(onConflict = REPLACE)
    suspend fun insertSeasonPlayers(entity: List<SeasonPlayerEntity>)
}
