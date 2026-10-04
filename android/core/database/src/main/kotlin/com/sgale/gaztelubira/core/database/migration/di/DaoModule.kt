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

package com.sgale.gaztelubira.core.database.migration.di

import com.sgale.gaztelubira.core.database.migration.GazteluDatabase
import com.sgale.gaztelubira.core.database.migration.match.MatchDao
import com.sgale.gaztelubira.core.database.migration.player.PlayerDao
import com.sgale.gaztelubira.core.database.migration.season.SeasonDao
import com.sgale.gaztelubira.core.database.migration.team.TeamDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun provideMatchDao(
        database: GazteluDatabase
    ): MatchDao = database.getMatchDao()

    @Provides
    @Singleton
    fun providePlayerDao(
        database: GazteluDatabase
    ): PlayerDao = database.getPlayerDao()

    @Provides
    @Singleton
    fun provideSeasonDao(
        database: GazteluDatabase
    ): SeasonDao = database.getSeasonDao()

    @Provides
    @Singleton
    fun provideTeamDao(
        database: GazteluDatabase
    ): TeamDao = database.getTeamDao()
}
