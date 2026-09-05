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

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.home.HomeNavigation.Companion.HomeNavigationContent
import com.sgale.gaztelubira.core.screens.home.HomeNavigation.Companion.rememberHomeNavigationState
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.home.HomeActions
import com.sgale.gaztelubira.multiplatform.ui.home.HomeView

@Composable
internal fun HomeScreen(
    navState: NavigationState,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
) {
    val mainViewModel = LocalMainViewModel.current
    val updateAvailable by mainViewModel.updateAvailable.collectAsState()
    val homeNavigation = rememberHomeNavigationState(mainViewModel.getDefaultTab())
    val selectedTab by homeNavigation.selectedTab
    val state by viewModel.state.collectAsStateWithLifecycle()

    val tabContent = remember {
        movableContentOf { HomeNavigationContent(homeNavigation, navState) }
    }

    val actions = remember(
        homeNavigation,
        mainViewModel,
        viewModel,
        navState
    ) {
        HomeActions(
            onTabSelected = homeNavigation::navigate,
            onLogout = { viewModel.logout(navState, mainViewModel) },
            onDismissLogout = { viewModel.updateLogoutDialog(false) }
        )
    }

    LaunchedEffect(updateAvailable) { viewModel.updateAvailableUpdate(updateAvailable) }

    LaunchedEffect(selectedTab) {
        viewModel.onTabChanged(selectedTab)
        mainViewModel.updateHomeTab(selectedTab)
    }

    BackHandler(true) { viewModel.updateLogoutDialog(true) }

    HomeView(state, actions, tabContent)
}
