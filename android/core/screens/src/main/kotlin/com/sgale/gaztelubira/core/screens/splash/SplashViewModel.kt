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

package com.sgale.gaztelubira.core.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.splash.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val STOP_TIMEOUT = 5_000L

/**
 * Unlike the other screens, the splash owns none of its state: `MainViewModel` drives the
 * app-scoped [SplashContractor] while the app boots, and this view model only projects that onto a
 * [SplashUiState]. The destination stays out of the state because only navigation reads it.
 */
@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val contractor: SplashContractor
) : ViewModel(), SplashScreenContract.ViewModel {

    internal val state: StateFlow<SplashUiState> = combine(
        contractor.completed,
        contractor.avoid
    ) { completed, avoid ->
        SplashUiState(completed = completed, avoid = avoid)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(STOP_TIMEOUT),
        initialValue = SplashUiState()
    )

    override fun navigate(state: NavigationState) {
        state.navigateTo(contractor.destination.value, clearStack = true)
    }
}
