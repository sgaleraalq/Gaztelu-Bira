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

package com.sgale.gaztelubira.core.database.migration

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgale.gaztelubira.core.database.migration.dao.MatchDao
import com.sgale.gaztelubira.core.database.migration.dao.PlayerDao
import com.sgale.gaztelubira.core.database.migration.dao.SeasonDao
import com.sgale.gaztelubira.core.database.migration.dao.TeamDao
import com.sgale.gaztelubira.core.database.migration.entity.MatchEntity
import com.sgale.gaztelubira.core.database.migration.entity.MatchEventEntity
import com.sgale.gaztelubira.core.database.migration.entity.MatchLineupPlayerEntity
import com.sgale.gaztelubira.core.database.migration.entity.PlayerEntity
import com.sgale.gaztelubira.core.database.migration.entity.SeasonEntity
import com.sgale.gaztelubira.core.database.migration.entity.SeasonPlayerEntity
import com.sgale.gaztelubira.core.database.migration.entity.TeamEntity

@Database(
    entities = [
        MatchEntity::class,
        MatchEventEntity::class,
        MatchLineupPlayerEntity::class,
        PlayerEntity::class,
        SeasonEntity::class,
        SeasonPlayerEntity::class,
        TeamEntity::class
    ],
    version = 11
)
internal abstract class GazteluDatabase : RoomDatabase() {
    abstract fun getMatchDao(): MatchDao
    abstract fun getPlayerDao(): PlayerDao
    abstract fun getSeasonDao(): SeasonDao
    abstract fun getTeamDao(): TeamDao
}
