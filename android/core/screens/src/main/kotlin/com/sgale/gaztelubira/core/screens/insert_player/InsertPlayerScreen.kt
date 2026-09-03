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

package com.sgale.gaztelubira.core.screens.insert_player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sgale.gaztelubira.core.domain.utils.rememberGalleryManager
import com.sgale.gaztelubira.core.screens.LocalMainViewModel
import com.sgale.gaztelubira.core.screens.R
import com.sgale.gaztelubira.core.screens.navigation.Destination.Home
import com.sgale.gaztelubira.core.screens.navigation.MultiplatformBackHandler
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.navigation.launchCameraAndWaitForResult
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Default
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Loading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.sgale.gaztelubira.multiplatform.designsystem.components.GBScaffold

@Composable
internal fun InsertPlayerScreen(
    state: NavigationState,
    viewModel: InsertPlayerViewModel = hiltViewModel<InsertPlayerViewModel>()
) {
    val notValidPlayerMsg = stringResource(R.string.not_valid_player_to_insert)
    val permissionDeniedCamera = stringResource(R.string.permission_denied_camera)
    val permissionDeniedGallery = stringResource(R.string.permission_denied_gallery)
    val uploadErrorMsg = stringResource(R.string.upload_error_message)

    val mainViewModel = LocalMainViewModel.current
    val user by mainViewModel.userSession.collectAsState()

    val data by viewModel.data.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    MultiplatformBackHandler(uiState != Loading) {
        state.navigateBack()
    }

    val galleryManager = rememberGalleryManager { commonImage ->
        viewModel.updatePicture(commonImage)
    }

    val launchGallery = {
        viewModel.initGallery(
            permissionDeniedMsg = permissionDeniedGallery,
            launchGallery = { galleryManager.launch() }
        )
    }

    val launchCamera = {
        viewModel.updateState(Default)
        viewModel.initCamera(
            permissionDeniedMsg = permissionDeniedCamera,
            launchCamera = {
                CoroutineScope(Main).launch {
                    launchCameraAndWaitForResult(state = state) { viewModel.updatePicture(it) }
                }
            }
        )
    }

    val insertPlayer = {
        viewModel.insertNewPlayer(
            uploadErrorMsg = uploadErrorMsg,
            notValidPlayerMsg = notValidPlayerMsg,
            onSuccess = { state.navigateTo(Home, true) }
        )
    }

    GBScaffold(
        appTeam = user?.team,
        showTopAppBar = true,
        topBarTitle = stringResource(R.string.insert_new_player)
    ) { modifier ->
        InsertPlayerScreenUI(
            modifier = modifier,
            data = data,
            uiState = uiState,
            getAvDorsals = { viewModel.getDorsals() },
            updateUi = { viewModel.updateState(it) },
            updateField = { field, value -> viewModel.updateField(field, value) },
            onMediaClicked = { launchGallery() },
            onCameraClicked = { launchCamera() },
            removeImage = { viewModel.removeImage() },
            onInsert = { insertPlayer() }
        )
    }
}
