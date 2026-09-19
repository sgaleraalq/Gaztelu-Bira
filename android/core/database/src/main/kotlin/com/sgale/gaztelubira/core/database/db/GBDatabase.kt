/*
 * Designed and developed by 2026 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sgale.gaztelubira.core.database.db.dao.MatchesDao
import com.sgale.gaztelubira.core.database.db.dao.MatchesStatsDao
import com.sgale.gaztelubira.core.database.db.dao.PlayerStatsDao
import com.sgale.gaztelubira.core.database.db.dao.PlayersDao
import com.sgale.gaztelubira.core.database.db.dao.TeamsDao
import com.sgale.gaztelubira.core.database.db.entities.Converters
import com.sgale.gaztelubira.core.database.db.entities.MatchEntity
import com.sgale.gaztelubira.core.database.db.entities.MatchStatsEntity
import com.sgale.gaztelubira.core.database.db.entities.PlayerEntity
import com.sgale.gaztelubira.core.database.db.entities.PlayerStatsEntity
import com.sgale.gaztelubira.core.database.db.entities.TeamEntity

@Database(
    entities = [
        MatchEntity::class,
        MatchStatsEntity::class,
        PlayerEntity::class,
        PlayerStatsEntity::class,
        TeamEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class GBDatabase : RoomDatabase() {
    abstract fun getMatchesDao(): MatchesDao
    abstract fun getMatchesStatsDao(): MatchesStatsDao
    abstract fun getPlayersDao(): PlayersDao
    abstract fun getPlayersStatsDao(): PlayerStatsDao
    abstract fun getTeamsDao(): TeamsDao
}
