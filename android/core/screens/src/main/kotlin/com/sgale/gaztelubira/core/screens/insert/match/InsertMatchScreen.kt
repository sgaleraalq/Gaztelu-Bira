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

package com.sgale.gaztelubira.core.screens.insert.match

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.navigation.navigateTo
import com.sgale.gaztelubira.core.screens.showToast
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchActions
import com.sgale.gaztelubira.multiplatform.ui.insert.match.InsertMatchView

@Composable
internal fun InsertMatchScreen(
    navState: NavigationState,
    viewModel: InsertMatchViewModel = hiltViewModel<InsertMatchViewModel>()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler(state.state.isNotLoading()) {
        navState.navigateBack()
    }

    val actions = remember(
        viewModel,
        navState
    ) {
        InsertMatchActions(
            updateField = viewModel::updateField,
            updateDialogState = viewModel::updateDialogState,
            toggleSection = viewModel::toggleSection,
            pickPlayer = viewModel::pickPlayer,
            removePlayer = viewModel::removePlayer,
            insertMatch = { viewModel.insertMatch(navState) { message -> showToast(context, message) } },
            navigateTo = { destination -> navState.navigateTo(destination) },
            onMissingField = { message -> showToast(context, message) }
        )
    }

    InsertMatchView(
        state = state,
        actions = actions
    )
}
