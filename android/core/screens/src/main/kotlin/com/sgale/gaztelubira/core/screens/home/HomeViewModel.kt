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

package com.sgale.gaztelubira.core.screens.home

import androidx.lifecycle.ViewModel
import com.sgale.gaztelubira.core.domain.auth.usecase.Logout
import com.sgale.gaztelubira.core.screens.MainViewModel
import com.sgale.gaztelubira.core.screens.navigation.Destination.Login
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab
import com.sgale.gaztelubira.multiplatform.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val logout: Logout
): ViewModel(), HomeScreenContract.ViewModel {

    private val _state = MutableStateFlow(HomeUiState())
    internal val state: StateFlow<HomeUiState> = _state

    internal fun onTabChanged(tab: HomeTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    internal fun updateAvailableUpdate(updateAvailable: Boolean) {
        _state.update { it.copy(updateAvailable = updateAvailable) }
    }

    internal fun updateLogoutDialog(show: Boolean) {
        _state.update { it.copy(showLogoutDialog = show) }
    }

    override fun logout(state: NavigationState, mainViewModel: MainViewModel) {
        mainViewModel.reset()
        state.navigateTo(Login, true)
        logout()
    }
}
