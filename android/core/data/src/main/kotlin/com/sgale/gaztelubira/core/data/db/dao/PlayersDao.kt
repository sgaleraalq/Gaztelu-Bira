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
import com.sgale.gaztelubira.core.data.db.entities.PlayerEntity
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao: BaseDao<PlayerEntity> {
    @Insert(onConflict = REPLACE)
    override suspend fun insert(entity: PlayerEntity)

    @Query("SELECT * FROM PlayerEntity")
    override fun getListAsFlow(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM PlayerEntity WHERE id = :id")
    override suspend fun getItem(id: FirebaseId): PlayerEntity?

    @Query("DELETE FROM PlayerEntity WHERE id = :id")
    override suspend fun deleteItem(id: FirebaseId)

    @Query("SELECT dorsal FROM PlayerEntity")
    suspend fun getDorsals(): List<Int>

    @Query("SELECT * FROM PlayerEntity")
    suspend fun getPlayers(): List<PlayerEntity>
}
