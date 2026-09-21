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

package com.sgale.gaztelubira.core.database.migrations

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgale.gaztelubira.core.database.migrations.dao.PlayerDao
import com.sgale.gaztelubira.core.database.migrations.entity.player.PlayerEntity

@Database(
    entities = [PlayerEntity::class],
    version = 1
)
internal abstract class Database : RoomDatabase() {
    abstract fun getPlayerDao(): PlayerDao
}
