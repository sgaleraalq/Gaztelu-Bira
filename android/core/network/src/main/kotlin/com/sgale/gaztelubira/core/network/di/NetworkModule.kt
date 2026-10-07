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

package com.sgale.gaztelubira.core.network.di

import com.sgale.gaztelubira.core.domain.migration.repository.MatchRemote
import com.sgale.gaztelubira.core.domain.migration.repository.PlayerRemote
import com.sgale.gaztelubira.core.domain.migration.repository.SeasonRemote
import com.sgale.gaztelubira.core.domain.migration.repository.StorageRemote
import com.sgale.gaztelubira.core.domain.migration.repository.TeamRemote
import com.sgale.gaztelubira.core.domain.migration.repository.UsersRemote
import com.sgale.gaztelubira.core.network.firebase.implementation.FirebaseStorage
import com.sgale.gaztelubira.core.network.firebase.implementation.FirebaseUsers
import com.sgale.gaztelubira.core.network.firebase.implementation.FirestoreMatches
import com.sgale.gaztelubira.core.network.firebase.implementation.FirestorePlayers
import com.sgale.gaztelubira.core.network.firebase.implementation.FirestoreSeason
import com.sgale.gaztelubira.core.network.firebase.implementation.FirestoreTeam
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
    internal fun provideMatchRemote(
        firestoreMatches: FirestoreMatches
    ): MatchRemote = firestoreMatches

    @Provides
    @Singleton
    internal fun providePlayerRemote(
        firestorePlayers: FirestorePlayers
    ): PlayerRemote = firestorePlayers

    @Provides
    @Singleton
    internal fun provideSeasonRemote(
        firestoreSeason: FirestoreSeason
    ): SeasonRemote = firestoreSeason

    @Provides
    @Singleton
    internal fun provideStorage(
        firebaseStorage: FirebaseStorage
    ): StorageRemote = firebaseStorage

    @Provides
    @Singleton
    internal fun provideTeamRemote(
        firestoreTeam: FirestoreTeam
    ): TeamRemote = firestoreTeam

    @Provides
    @Singleton
    internal fun provideFirebaseUsers(
        firebaseUsers: FirebaseUsers
    ): UsersRemote = firebaseUsers
}
