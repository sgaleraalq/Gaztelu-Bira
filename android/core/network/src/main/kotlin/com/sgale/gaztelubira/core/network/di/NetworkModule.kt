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

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.sgale.gaztelubira.core.domain.repository.firestore.IFbUsers
import com.sgale.gaztelubira.core.domain.repository.IAppVersion
import com.sgale.gaztelubira.core.domain.repository.firestore.IFetch
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBFireStorage
import com.sgale.gaztelubira.core.domain.repository.firestore.IInsert
import com.sgale.gaztelubira.core.network.R
import com.sgale.gaztelubira.core.network.firebase.implementation.remote_config.RemoteConfigManager.Companion.MINIMUM_FETCH_INTERVAL_SECONDS
import com.sgale.gaztelubira.core.network.firebase.implementation.FirebaseUsers
import com.sgale.gaztelubira.core.network.firebase.implementation.fetch.FirebaseFetch
import com.sgale.gaztelubira.core.network.firebase.implementation.remote_config.RemoteConfigAppVersion
import com.sgale.gaztelubira.core.network.firebase.implementation.insert.FirebaseInsert
import com.sgale.gaztelubira.core.network.firebase.implementation.storage.FirebaseStorage
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
    fun provideFirebaseFetch(firebaseFetch: FirebaseFetch): IFetch =
        firebaseFetch

    @Provides
    @Singleton
    fun provideFirebaseInsert(firebaseInsert: FirebaseInsert): IInsert =
        firebaseInsert

    @Provides
    @Singleton
    fun provideFirebaseStorage(firebaseStorage: FirebaseStorage): IGBFireStorage =
        firebaseStorage

    @Provides
    @Singleton
    fun provideFirebaseUsers(firebaseUsers: FirebaseUsers): IFbUsers =
        firebaseUsers

    @Provides
    @Singleton
    fun provideAppVersion(remoteConfigAppVersion: RemoteConfigAppVersion): IAppVersion =
        remoteConfigAppVersion

    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig =
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = MINIMUM_FETCH_INTERVAL_SECONDS
                }
            )
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
}
