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

package com.sgale.gaztelubira.core.screens.di

import com.sgale.gaztelubira.core.domain.utils.PermissionBridge
import com.sgale.gaztelubira.core.domain.utils.SharedImagesBridge
import com.sgale.gaztelubira.core.screens.splash.SplashContractor
import com.sgale.gaztelubira.core.screens.splash.SplashState
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ScreensModule {

    @Binds
    @Singleton
    fun bindSplashContractor(splashState: SplashState): SplashContractor
}

@Module
@InstallIn(SingletonComponent::class)
internal object ScreensProvidersModule {

    @Provides
    @Singleton
    fun providePermissionBridge(): PermissionBridge = PermissionBridge()

    @Provides
    @Singleton
    fun provideSharedImagesBridge(): SharedImagesBridge = SharedImagesBridge()
}
