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

package com.sgale.gaztelubira.core.database.migration.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.sgale.gaztelubira.core.database.migration.GazteluDatabase
import com.sgale.gaztelubira.core.database.migration.player.PlayerDao
import com.sgale.gaztelubira.core.database.migration.player.RoomPlayers
import com.sgale.gaztelubira.core.domain.migration.repository.player.PlayerLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val GAZTELU_DB = "gaztelu_bira_database"

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): GazteluDatabase =
        Room.databaseBuilder<GazteluDatabase>(context, GAZTELU_DB)
            .setDriver(AndroidSQLiteDriver())
            .build()

    @Provides
    @Singleton
    fun providePlayerDao(
        database: GazteluDatabase
    ): PlayerDao = database.getPlayerDao()

    @Provides
    @Singleton
    fun providePlayerLocal(
        roomPlayers: RoomPlayers
    ): PlayerLocal = roomPlayers
}
