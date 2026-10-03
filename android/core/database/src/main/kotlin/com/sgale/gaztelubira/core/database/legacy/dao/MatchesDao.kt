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

package com.sgale.gaztelubira.core.database.legacy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.sgale.gaztelubira.core.database.legacy.entities.match.LegacyMatchEntity
import com.sgale.gaztelubira.core.domain.legacy.model.match.MatchType.LEAGUE
import com.sgale.gaztelubira.core.domain.legacy.model.utils.FirebaseId
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchesDao: BaseDao<LegacyMatchEntity> {
    @Insert(onConflict = REPLACE)
    override suspend fun insert(entity: LegacyMatchEntity)

    @Query("SELECT * FROM LegacyMatchEntity")
    override fun getListAsFlow(): Flow<List<LegacyMatchEntity>>

    @Query("SELECT * FROM LegacyMatchEntity WHERE id = :id")
    override suspend fun getItem(id: FirebaseId): LegacyMatchEntity?

    @Query("DELETE FROM LegacyMatchEntity WHERE id = :id")
    override suspend fun deleteItem(id: FirebaseId)

    @Query("SELECT * FROM LegacyMatchEntity WHERE matchType = :type")
    suspend fun getNumberOfJourneys(type: String = LEAGUE.name): List<LegacyMatchEntity>

    @Query("SELECT * FROM LegacyMatchEntity")
    suspend fun getMatches(): List<LegacyMatchEntity>
}
