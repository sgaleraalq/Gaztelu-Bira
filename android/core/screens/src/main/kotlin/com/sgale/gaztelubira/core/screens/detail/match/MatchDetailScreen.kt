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

package com.sgale.gaztelubira.core.screens.detail.match

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import com.sgale.gaztelubira.core.screens.navigation.Destination.Companion.navigateTo
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailActions
import com.sgale.gaztelubira.multiplatform.ui.detail.match.MatchDetailView

@Composable
internal fun MatchDetailScreen(
    navState: NavigationState,
    matchId: FirebaseId,
    viewModel: MatchDetailViewModel = hiltViewModel<MatchDetailViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.loadMatch(navState, matchId)
    }

    val actions = remember(
        viewModel,
        navState
    ) {
        MatchDetailActions(
            changeUiState = viewModel::changeUiState,
            navigateTo = { destination -> navState.navigateTo(destination) }
        )
    }

    MatchDetailView(
        state = state,
        actions = actions
    )
}
