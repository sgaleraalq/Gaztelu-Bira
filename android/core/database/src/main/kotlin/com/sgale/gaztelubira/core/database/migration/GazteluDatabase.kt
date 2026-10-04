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
import com.sgale.gaztelubira.core.database.migration.match.MatchDao
import com.sgale.gaztelubira.core.database.migration.match.entity.MatchEntity
import com.sgale.gaztelubira.core.database.migration.player.PlayerDao
import com.sgale.gaztelubira.core.database.migration.player.PlayerEntity
import com.sgale.gaztelubira.core.database.migration.season.SeasonDao
import com.sgale.gaztelubira.core.database.migration.season.SeasonEntity
import com.sgale.gaztelubira.core.database.migration.season.entity.SeasonPlayerEntity
import com.sgale.gaztelubira.core.database.migration.team.TeamDao
import com.sgale.gaztelubira.core.database.migration.team.entity.TeamEntity

@Database(
    entities = [
        MatchEntity::class,
        PlayerEntity::class,
        SeasonEntity::class,
        SeasonPlayerEntity::class,
        TeamEntity::class
    ],
    version = 4
)
internal abstract class GazteluDatabase : RoomDatabase() {
    abstract fun getMatchDao(): MatchDao
    abstract fun getPlayerDao(): PlayerDao
    abstract fun getSeasonDao(): SeasonDao
    abstract fun getTeamDao(): TeamDao
}
