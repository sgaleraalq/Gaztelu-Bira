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

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.navigation.NavigationState
import com.sgale.gaztelubira.core.navigation.tab.TabNavigation
import com.sgale.gaztelubira.core.navigation.tab.rememberTabNavigationState
import com.sgale.gaztelubira.core.screens.openStoreListing
import com.sgale.gaztelubira.multiplatform.ui.home.HomeActions
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab
import com.sgale.gaztelubira.multiplatform.ui.home.HomeView

@Composable
internal fun HomeScreen(
    navState: NavigationState,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
) {
    val context = LocalContext.current
    val homeNavigation = rememberTabNavigationState(HomeTab.entries, DEFAULT_TAB)
    val selectedTab by homeNavigation.selectedTab
    val state by viewModel.state.collectAsStateWithLifecycle()

    val tabContent = remember {
        movableContentOf {
            TabNavigation(homeNavigation) { tab -> tab.Content(navState) }
        }
    }

    val actions = remember(
        context,
        homeNavigation,
        viewModel,
        navState
    ) {
        HomeActions(
            onTabSelected = homeNavigation::navigate,
            onLogout = { viewModel.logout(navState) },
            onDismissLogout = { viewModel.updateLogoutDialog(false) },
            onUpdate = { context.openStoreListing() }
        )
    }

    LaunchedEffect(selectedTab) { viewModel.onTabChanged(selectedTab) }

    BackHandler(true) { viewModel.updateLogoutDialog(true) }

    HomeView(state, actions, tabContent)
}
