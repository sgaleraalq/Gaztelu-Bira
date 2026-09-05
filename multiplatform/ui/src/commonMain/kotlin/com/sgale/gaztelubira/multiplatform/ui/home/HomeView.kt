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

package com.sgale.gaztelubira.multiplatform.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBUpdateDialog
import com.sgale.gaztelubira.multiplatform.ui.home.HomeTab.ABOUT

@Composable
fun HomeView(
//    state: NavigationState,
//    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
) {
//    val mainViewModel = LocalMainViewModel.current
//    val updateAvailable by mainViewModel.updateAvailable.collectAsState()
//
//    val homeNavigationState = rememberHomeNavigationState(mainViewModel.getDefaultTab())
    val tabContent = remember {
        movableContentOf { HomeNavigationContent(homeNavigationState, state) }
    }
//    var showLogoutDialog by remember { mutableStateOf(false) }
//    MultiplatformBackHandler(true) {
//        showLogoutDialog = true
//    }

    HomeViewUI(
        bottomTabs = HomeTab.entries,
        selectedTab = homeNavigationState.selectedTab,
        navigate = { newTab ->
            mainViewModel.updateHomeTab(newTab)
            if (newTab != ABOUT) {
                homeNavigationState.navigate(newTab)
            } else {
                // TODO

            }
        },
        showLogoutDialog = showLogoutDialog,
        onLogout = { viewModel.logout(state, mainViewModel) },
        dismissLogout = { showLogoutDialog = false }
    ) {
        tabContent()
    }

    if (updateAvailable) {
        GBUpdateDialog()
    }
}
