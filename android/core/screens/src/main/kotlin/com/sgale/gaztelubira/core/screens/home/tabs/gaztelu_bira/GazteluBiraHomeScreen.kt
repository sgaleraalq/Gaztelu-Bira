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

package com.sgale.gaztelubira.core.screens.home.tabs.gaztelu_bira

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.navigation.Destination.InsertTeam
import com.sgale.gaztelubira.core.screens.navigation.NavigationState

@Composable
fun GazteluBiraHomeScreen(
    state: NavigationState,
    viewModel: GazteluBiraHomeViewModel = hiltViewModel<GazteluBiraHomeViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val userSession by mainViewModel.userSession.collectAsState()

    val teams by viewModel.teams.collectAsState()
    val gbInformation by viewModel.gbInformation.collectAsState()

    LaunchedEffect(userSession) {
        userSession?.let { viewModel.startHandler(it.team) }
    }

    GazteluBiraHomeUI(
        user = userSession,
        teams = teams,
        gbInformation = gbInformation,
        navigateToInsertTeam = { state.navigateTo(InsertTeam) }
    )
}
