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

package com.sgale.gaztelubira.core.network.di

import com.google.firebase.firestore.FirebaseFirestore
import com.sgale.gaztelubira.core.domain.repository.db.IGBPreferences
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFetchDataFb
import com.sgale.gaztelubira.core.network.firebase.implementation.FbFetchDataImpl
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchMatches
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchMatchesStats
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchPlayerStats
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchPlayers
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchTeam
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchTeams
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FetchTimestamp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideFirebaseFetch(
        firestore: FirebaseFirestore,
        gbSettings: IGBPreferences
    ): IGBFetchDataFb =
        FbFetchDataImpl(
            fetchMatches = FetchMatches(firestore, gbSettings),
            fetchMatchesStats = FetchMatchesStats(firestore, gbSettings),
            fetchPlayers = FetchPlayers(firestore, gbSettings),
            fetchPlayerStats = FetchPlayerStats(firestore, gbSettings),
            fetchTeams = FetchTeams(firestore, gbSettings),
            fetchTeam = FetchTeam(firestore, gbSettings),
            fetchTimestamp = FetchTimestamp(firestore, gbSettings)
        )
}
