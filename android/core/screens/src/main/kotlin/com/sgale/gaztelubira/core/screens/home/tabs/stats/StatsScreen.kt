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

package com.sgale.gaztelubira.core.screens.home.tabs.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsActions
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.stats.StatsView

@Composable
internal fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel<StatsViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val actions = remember(viewModel) {
        StatsActions(
            onPlayerSelected = viewModel::onPlayerSelected,
            onPlayerDismissed = viewModel::onPlayerDismissed,
            onSettingsChanged = viewModel::onSettingsChanged,
            onStatSelected = viewModel::onStatSelected,
            onPunctuationDraftChanged = viewModel::onPunctuationDraftChanged,
            onPunctuationConfirmed = viewModel::onPunctuationConfirmed
        )
    }

    StatsView(state, actions)
}
