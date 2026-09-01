/*
 * Designed and developed by 2025 sgaleraalq (Sergio Galera)
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

package com.sgale.gaztelubira.core.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@Composable
fun SplashScreen(state: NavigationState) {
    val contractor = EntryPointAccessors
        .fromApplication<SplashContractorEntryPoint>(LocalContext.current.applicationContext)
        .splashContractor()
    val avoid by contractor.avoid.collectAsState()
    val completed by contractor.completed.collectAsState()
    val destination by contractor.destination.collectAsState()

    SplashScreenUI(completed, avoid) { state.navigateTo(destination, clearStack = true) }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface SplashContractorEntryPoint {
    fun splashContractor(): SplashContractor
}
