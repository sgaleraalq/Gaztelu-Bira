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

package com.sgale.gaztelubira.multiplatform.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBLogoutDialog
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBScaffold
import com.sgale.gaztelubira.multiplatform.ui.home.ui.GBBottomNavigation
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.logout
import com.sgale.gaztelubira.multiplatform.ui.resources.no
import com.sgale.gaztelubira.multiplatform.ui.resources.sure_want_to_logout
import com.sgale.gaztelubira.multiplatform.ui.resources.yes
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeViewUI(
    state: HomeUiState,
    actions: HomeActions,
    tabContent: @Composable () -> Unit
) {
    val bottomBar = @Composable {
        GBBottomNavigation(
            tabs = HomeTab.entries,
            selectedTab = state.selectedTab,
            navigate = actions.onTabSelected
        )
    }

    val content: @Composable (Modifier) -> Unit = { modifier ->
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            tabContent()
        }

        GBLogoutDialog(
            show = state.showLogoutDialog,
            onConfirm = actions.onLogout,
            onCancel = actions.onDismissLogout
        )
    }

    GBScaffold(
        bottomBar = bottomBar,
        content = content
    )
}
