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

package com.sgale.gaztelubira.core.network.migration.firebase

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.firebase.storage.storage
import com.sgale.gaztelubira.core.domain.auth.IAuthRepository
import com.sgale.gaztelubira.core.domain.auth.IGoogleSignIn
import com.sgale.gaztelubira.core.network.R
import com.sgale.gaztelubira.core.network.auth.AndroidGoogleSignIn
import com.sgale.gaztelubira.core.network.auth.AuthRepositoryImpl
import com.sgale.gaztelubira.core.network.auth.GoogleClientId
import com.sgale.gaztelubira.core.network.migration.firebase.remote_config.RemoteConfigManager.Companion.MINIMUM_FETCH_INTERVAL_SECONDS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseModule {
    /**
     * Auth
     */
    @Provides
    @Singleton
    internal fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): IAuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    internal fun provideGoogleSignIn(
        @ApplicationContext context: Context,
        @GoogleClientId clientId: String,
        credentialManager: CredentialManager,
    ): IGoogleSignIn = AndroidGoogleSignIn(context, credentialManager, clientId)

    @Provides
    @Singleton
    internal fun provideCredentialManager(
        @ApplicationContext context: Context
    ): CredentialManager = CredentialManager.create(context)

    /**
     * Firebase
     */
    @Provides
    @Singleton
    internal fun provideFireStorage() = Firebase.storage

    @Provides
    @Singleton
    internal fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    internal fun provideRemoteConfig(): FirebaseRemoteConfig =
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = MINIMUM_FETCH_INTERVAL_SECONDS
                }
            )
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
}
