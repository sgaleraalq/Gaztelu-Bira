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

package com.sgale.gaztelubira.core.screens.insert.team

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.domain.utils.rememberGalleryManager
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamActions
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamView
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamState.Companion.isNotLoading

@Composable
internal fun InsertTeamScreen(
    navState: NavigationState,
    viewModel: InsertTeamViewModel = hiltViewModel<InsertTeamViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val errorMsg = stringResource(R.string.upload_error_message)

    BackHandler(state.state.isNotLoading()) {
        navState.navigateBack()
    }

    val galleryManager = rememberGalleryManager { commonImage ->
        viewModel.onImagePicked(commonImage)
    }

    val actions = remember(
        viewModel,
        navState,
        galleryManager,
        errorMsg
    ) {
        InsertTeamActions(
            updateField = viewModel::updateField,
            pickImage = galleryManager::launch,
            insertTeam = { viewModel.insertTeam(navState, errorMsg) }
        )
    }

    InsertTeamView(
        state = state,
        actions = actions
    )
}
