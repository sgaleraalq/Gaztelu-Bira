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

import android.content.Context
import androidx.credentials.CredentialManager
import com.sgale.gaztelubira.core.domain.auth.IAuthRepository
import com.sgale.gaztelubira.core.domain.auth.IGoogleSignIn
import com.sgale.gaztelubira.core.network.auth.AndroidGoogleSignIn
import com.sgale.gaztelubira.core.network.auth.AuthRepositoryImpl
import com.sgale.gaztelubira.core.network.auth.GoogleClientId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): IAuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    fun provideGoogleSignIn(
        @ApplicationContext context: Context,
        @GoogleClientId clientId: String,
        credentialManager: CredentialManager,
    ): IGoogleSignIn = AndroidGoogleSignIn(context, credentialManager, clientId)

    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext context: Context
    ): CredentialManager = CredentialManager.create(context)
}
