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

package com.sgale.gaztelubira.core.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBLogoutDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBScaffold

@Composable
fun HomeScreenUI(
    bottomTabs: List<HomeTab>,
    selectedTab: State<HomeTab>,
    navigate: (HomeTab) -> Unit,
    showLogoutDialog: Boolean,
    dismissLogout: () -> Unit,
    onLogout: () -> Unit,
    tabContent: @Composable () -> Unit
) {
    val mainViewModel = LocalMainViewModel.current
    val user by mainViewModel.userSession.collectAsState()

    GBScaffold(
        topBarTitle = "",
        bottomBar = {
            GBBottomNavigation(bottomTabs, selectedTab) { navigate(it) }
        }
    ) { modifier ->
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            tabContent.invoke()
        }

        if (showLogoutDialog) {
            GBLogoutDialog(
                appTeam = user?.team,
                onConfirm = { onLogout() },
                onCancel = { dismissLogout() },
                logout = stringResource(R.string.logout),
                sureLogout = stringResource(R.string.sure_want_to_logout),
                no = stringResource(R.string.no),
                yes = stringResource(R.string.yes),
            )
        }
    }
}
