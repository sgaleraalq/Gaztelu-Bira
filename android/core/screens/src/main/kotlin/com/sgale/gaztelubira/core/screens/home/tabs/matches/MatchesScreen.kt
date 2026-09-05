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

package com.sgale.gaztelubira.core.screens.home.tabs.matches

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.navigation.NavigationState

@Composable
fun MatchesScreen(
    state: NavigationState,
    viewModel: MatchesViewModel = hiltViewModel<MatchesViewModel>(),
) {
    val mainViewModel = LocalMainViewModel.current
    val userSession by mainViewModel.userSession.collectAsState()

    val matches by viewModel.matches.collectAsState()
    val enoughPlayers by viewModel.enoughPlayers.collectAsState()

    MatchesScreenUI(
        user = userSession,
        matches = matches,
        hasEnoughPlayers = enoughPlayers,
        calculateResult = GetMatchResultUseCase(),
        navigateTo = { state.navigateTo(it) }
    )
}
