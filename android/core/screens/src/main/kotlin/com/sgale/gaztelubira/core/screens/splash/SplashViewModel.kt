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
import com.sgale.gaztelubira.core.screens.splash.SplashStatus.Finished
import com.sgale.gaztelubira.core.screens.splash.SplashStatus.Finished.Completed
import com.sgale.gaztelubira.core.screens.splash.SplashStatus.Finished.Skipped
import com.sgale.gaztelubira.core.screens.splash.SplashStatus.Loading
import com.sgale.gaztelubira.multiplatform.ui.splash.SplashUiState
import com.sgale.gaztelubira.multiplatform.ui.splash.state.SplashPhase
import com.sgale.gaztelubira.multiplatform.ui.splash.state.SplashPhase.COMPLETING
import com.sgale.gaztelubira.multiplatform.ui.splash.state.SplashPhase.LOADING
import com.sgale.gaztelubira.multiplatform.ui.splash.state.SplashPhase.SKIPPED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val STOP_TIMEOUT = 5_000L

/**
 * Unlike the other screens, the splash owns none of its state: `MainViewModel` drives the
 * app-scoped [SplashController] while the app boots, and this view model only projects that onto a
 * [SplashUiState]. The destination stays out of the state because only navigation reads it, and it
 * is read from the status itself so it cannot be picked up before boot has decided on one.
 */
@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val controller: SplashController
) : ViewModel() {

    internal val state: StateFlow<SplashUiState> = controller.status
        .map { SplashUiState(it.toPhase()) }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(STOP_TIMEOUT),
            initialValue = SplashUiState(controller.status.value.toPhase())
        )

    internal fun navigate(state: NavigationState) {
        val status = controller.status.value
        if (status !is Finished) return

        state.navigateTo(
            destination = status.destination,
            clearStack = true
        )
    }
}

private fun SplashStatus.toPhase(): SplashPhase =
    when (this) {
        Loading -> LOADING
        is Completed -> COMPLETING
        is Skipped -> SKIPPED
    }
