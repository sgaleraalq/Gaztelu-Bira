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

package com.sgale.gaztelubira.core.database.di

import com.sgale.gaztelubira.core.database.AppHandler
import com.sgale.gaztelubira.core.database.AppRepositoryImpl
import com.sgale.gaztelubira.core.database.db.implementations.AbstractGBDb
import com.sgale.gaztelubira.core.database.db.implementations.GBMatchesDb
import com.sgale.gaztelubira.core.database.db.implementations.GBMatchesStatsDb
import com.sgale.gaztelubira.core.database.db.implementations.GBPlayerStatsDb
import com.sgale.gaztelubira.core.database.db.implementations.GBPlayersDb
import com.sgale.gaztelubira.core.database.db.implementations.GBTeamsDb
import com.sgale.gaztelubira.core.database.preferences.GBSettings
import com.sgale.gaztelubira.core.domain.repository.IAppRepository
import com.sgale.gaztelubira.core.domain.repository.InitAppHandler
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBMatchesStatsDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBPlayersStatsDb
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.db.IGBTeamsDb
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    /**
     * Database
     */
    @Binds
    @Singleton
    fun bindAbstractGBDb(gbMatchesDb: GBMatchesDb): AbstractGBDb

    @Binds
    @Singleton
    fun bindMatchesDb(gbMatchesDb: GBMatchesDb): IGBMatchesDb

    @Binds
    @Singleton
    fun bindMatchesStatsDb(gbMatchesStatsDb: GBMatchesStatsDb): IGBMatchesStatsDb

    @Binds
    @Singleton
    fun bindPlayersDb(gbPlayersDb: GBPlayersDb): IGBPlayersDb

    @Binds
    @Singleton
    fun bindPlayersStatsDb(gbPlayerStatsDb: GBPlayerStatsDb): IGBPlayersStatsDb

    @Binds
    @Singleton
    fun bindTeamsDb(gbTeamsDb: GBTeamsDb): IGBTeamsDb

    @Binds
    @Singleton
    fun bindPreferences(gbSettings: GBSettings): IGBPreferences

    /**
     * Network
     */
    @Binds
    @Singleton
    fun bindAppRepository(appRepositoryImpl: AppRepositoryImpl): IAppRepository

    /**
     * App
     */
    @Binds
    @Singleton
    fun bindInitAppHandler(appHandler: AppHandler): InitAppHandler
}
