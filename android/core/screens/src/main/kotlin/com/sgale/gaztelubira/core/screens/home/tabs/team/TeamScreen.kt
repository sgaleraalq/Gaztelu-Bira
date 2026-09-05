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

package com.sgale.gaztelubira.core.screens.home.tabs.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.screens.navigation.Destination.Companion.toDestination
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamActions
import com.sgale.gaztelubira.multiplatform.ui.home.tabs.team.TeamView

@Composable
fun TeamScreen(
    navState: NavigationState,
    viewModel: TeamViewModel = hiltViewModel<TeamViewModel>()
) {
    val state by viewModel.state.collectAsState()

    val actions = remember(
        navState,
        viewModel
    ) {
        TeamActions(
            navigateTo = { destination -> navState.navigateTo(destination.toDestination()) }
        )
    }

    TeamView(state, actions)
}
