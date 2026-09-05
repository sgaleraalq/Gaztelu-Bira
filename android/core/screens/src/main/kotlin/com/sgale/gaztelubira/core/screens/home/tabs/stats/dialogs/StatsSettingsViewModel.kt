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

package com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs

import androidx.lifecycle.ViewModel
import com.sgale.gaztelubira.core.screens.home.tabs.stats.data.Punctuation
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.SettingsDialogState.ChangePunctuation
import com.sgale.gaztelubira.core.screens.home.tabs.stats.dialogs.SettingsDialogState.Default
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class StatsSettingsViewModel @Inject constructor(): ViewModel() {
    private val _dialogState = MutableStateFlow<SettingsDialogState>(Default)
    val dialogState: StateFlow<SettingsDialogState> = _dialogState

    fun changeState(newState: SettingsDialogState) {
        _dialogState.value = newState
    }

    fun updatePunctuation(punctuation: Punctuation) {
        _dialogState.value = ChangePunctuation(punctuation)
    }
}
