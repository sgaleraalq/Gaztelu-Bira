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
import com.sgale.gaztelubira.core.data.db.entities.MatchEntity
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.domain.model.utils.GBConstants.LEAGUE
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchesDao: BaseDao<MatchEntity> {
    @Insert(onConflict = REPLACE)
    override suspend fun insert(entity: MatchEntity)

    @Query("SELECT * FROM MatchEntity")
    override fun getListAsFlow(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM MatchEntity WHERE id = :id")
    override suspend fun getItem(id: FirebaseId): MatchEntity?

    @Query("DELETE FROM MatchEntity WHERE id = :id")
    override suspend fun deleteItem(id: FirebaseId)

    @Query("SELECT * FROM MatchEntity WHERE matchType = :type")
    suspend fun getNumberOfJourneys(type: String = LEAGUE): List<MatchEntity>

    @Query("SELECT * FROM MatchEntity")
    suspend fun getMatches(): List<MatchEntity>
}
