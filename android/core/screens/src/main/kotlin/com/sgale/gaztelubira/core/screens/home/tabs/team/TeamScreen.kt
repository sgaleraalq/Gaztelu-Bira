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

package com.sgale.gaztelubira.core.screens.home.tabs.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.navigation.Destination.InsertPlayer
import com.sgale.gaztelubira.core.screens.navigation.Destination.PlayerDetail
import com.sgale.gaztelubira.core.screens.navigation.NavigationState

@Composable
fun TeamScreen(
    state: NavigationState,
    viewModel: TeamViewModel = hiltViewModel<TeamViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val user by mainViewModel.userSession.collectAsState()

    val players by viewModel.players.collectAsState()

    TeamScreenUI(
        appTeam = user?.team,
        players = players,
        isAdmin = user?.isAdmin(),
        navigateToInsertPlayer = { state.navigateTo(InsertPlayer) },
        navigateToPlayerDetail = { id, isManager -> state.navigateTo(PlayerDetail(id, isManager)) }
    )
}
