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

package com.sgale.gaztelubira.core.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.InitAppHandler
import com.sgale.gaztelubira.core.domain.auth.usecase.Logout
import com.sgale.gaztelubira.core.navigation.Destination.Login
import com.sgale.gaztelubira.core.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.STATS
import com.sgale.gaztelubira.multiplatform.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal val DEFAULT_TAB = STATS

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val logout: Logout,
    initAppHandler: InitAppHandler
): ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(selectedTab = DEFAULT_TAB))
    internal val state: StateFlow<HomeUiState> = _state

    init {
        viewModelScope.launch {
            val updateAvailable = withContext(Dispatchers.IO) { initAppHandler.updateAvailable() }
            _state.update { it.copy(updateAvailable = updateAvailable) }
        }
    }

    internal fun onTabChanged(tab: HomeTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    internal fun updateLogoutDialog(show: Boolean) {
        _state.update { it.copy(showLogoutDialog = show) }
    }

    internal fun logout(state: NavigationState) {
        state.navigateTo(Login, true)
        logout()
    }
}
