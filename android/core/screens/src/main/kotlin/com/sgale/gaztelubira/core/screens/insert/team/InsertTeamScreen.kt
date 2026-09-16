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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.screens.insert.manager.gallery.rememberGalleryManager
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.showToast
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamActions
import com.sgale.gaztelubira.multiplatform.ui.insert.team.InsertTeamView
import com.sgale.gaztelubira.multiplatform.ui.insert.team.state.InsertTeamField.TeamImage
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.permission_denied_gallery
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InsertTeamScreen(
    navState: NavigationState,
    viewModel: InsertTeamViewModel = hiltViewModel<InsertTeamViewModel>()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val permissionDeniedMsg = stringResource(Res.string.permission_denied_gallery)

    BackHandler(state.state.isNotLoading()) {
        navState.navigateBack()
    }

    val galleryManager = rememberGalleryManager(
        onPermissionDenied = { showToast(context, permissionDeniedMsg) }
    ) { uri ->
        viewModel.updateField(TeamImage(uri.toString()))
    }

    val actions = remember(
        viewModel,
        navState,
        galleryManager
    ) {
        InsertTeamActions(
            updateField = viewModel::updateField,
            openGallery = galleryManager::launch,
            insertTeam = { viewModel.insertTeam(navState) },
            onMissingField = { message -> showToast(context, message)}
        )
    }

    InsertTeamView(
        state = state,
        actions = actions
    )
}
