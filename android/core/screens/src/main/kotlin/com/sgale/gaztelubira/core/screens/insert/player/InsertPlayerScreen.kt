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

package com.sgale.gaztelubira.core.screens.insert.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sgale.gaztelubira.core.domain.utils.CommonImage.FromGallery
import com.sgale.gaztelubira.core.screens.insert.manager.gallery.rememberGalleryManager
import com.sgale.gaztelubira.core.screens.insert.manager.permissions.MediaPermission.CAMERA
import com.sgale.gaztelubira.core.screens.insert.manager.permissions.rememberPermissionsManager
import com.sgale.gaztelubira.core.screens.navigation.MultiplatformBackHandler
import com.sgale.gaztelubira.core.screens.navigation.NavigationState
import com.sgale.gaztelubira.core.screens.navigation.launchCameraAndWaitForResult
import com.sgale.gaztelubira.core.screens.showToast
import com.sgale.gaztelubira.multiplatform.ui.insert.InsertingDataState.Companion.isNotLoading
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerActions
import com.sgale.gaztelubira.multiplatform.ui.insert.player.InsertPlayerView
import com.sgale.gaztelubira.multiplatform.ui.resources.Res
import com.sgale.gaztelubira.multiplatform.ui.resources.permission_denied_camera
import com.sgale.gaztelubira.multiplatform.ui.resources.permission_denied_gallery
import com.sgale.gaztelubira.multiplatform.ui.resources.upload_error_message
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InsertPlayerScreen(
    navState: NavigationState,
    viewModel: InsertPlayerViewModel = hiltViewModel<InsertPlayerViewModel>()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val galleryDeniedMsg = stringResource(Res.string.permission_denied_gallery)
    val cameraDeniedMsg = stringResource(Res.string.permission_denied_camera)
    val uploadErrorMsg = stringResource(Res.string.upload_error_message)

    MultiplatformBackHandler(state.state.isNotLoading()) {
        navState.navigateBack()
    }

    val permissions = rememberPermissionsManager()

    val galleryManager = rememberGalleryManager(
        onPermissionDenied = { showToast(context, galleryDeniedMsg) }
    ) { uri ->
        viewModel.onImagePicked(
            FromGallery(
                uri = uri.toString(),
                mimeType = context.contentResolver.getType(uri)
            )
        )
    }

    /**
     * The camera is a destination of our own rather than a system picker, so it is navigated to and
     * its result awaited; the permission is asked for here because that screen opens the device
     * camera straight away.
     */
    val takePicture = {
        permissions.withPermission(
            permission = CAMERA,
            onDenied = { showToast(context, cameraDeniedMsg) }
        ) {
            scope.launch {
                launchCameraAndWaitForResult(state = navState) { image ->
                    viewModel.onImagePicked(image)
                }
            }
        }
    }

    val actions = remember(
        viewModel,
        navState,
        galleryManager,
        permissions
    ) {
        InsertPlayerActions(
            updateField = viewModel::updateField,
            showDialog = viewModel::showDialog,
            pickImage = galleryManager::launch,
            takePicture = takePicture,
            insertPlayer = {
                viewModel.insertPlayer(navState) { showToast(context, uploadErrorMsg) }
            },
            onMissingField = { message -> showToast(context, message) }
        )
    }

    InsertPlayerView(
        state = state,
        actions = actions
    )
}
