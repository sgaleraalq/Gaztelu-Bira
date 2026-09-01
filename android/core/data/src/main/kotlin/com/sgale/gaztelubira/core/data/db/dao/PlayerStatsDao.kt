/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.sgale.gaztelubira.core.data.db.entities.PlayerStatsEntity
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerStatsDao: BaseDao<PlayerStatsEntity> {
    @Insert(onConflict = REPLACE)
    override suspend fun insert(entity: PlayerStatsEntity)

    @Query("SELECT * FROM PlayerStatsEntity")
    override fun getListAsFlow(): Flow<List<PlayerStatsEntity>>

    @Query("SELECT * FROM PlayerStatsEntity WHERE id = :id")
    override suspend fun getItem(id: FirebaseId): PlayerStatsEntity?

    @Query("DELETE FROM PlayerStatsEntity WHERE id = :id")
    override suspend fun deleteItem(id: FirebaseId)
}
