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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgale.gaztelubira.core.domain.model.player.Position
import com.sgale.gaztelubira.core.domain.repository.firestore.IGBInsertDataFb.FirebaseInsertResult.PlayerInserted
import com.sgale.gaztelubira.core.domain.usecase.ShowCamera
import com.sgale.gaztelubira.core.domain.usecase.ShowGallery
import com.sgale.gaztelubira.core.domain.usecase.db.GetAvailableDorsals
import com.sgale.gaztelubira.core.domain.usecase.firestore.insert.InsertNewPlayer
import com.sgale.gaztelubira.core.domain.utils.CommonImage
import com.sgale.gaztelubira.core.domain.utils.IToastManager
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerData.PictureType
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerData.PictureType.Body
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerData.PictureType.Face
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.BodyImage
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.Dorsal
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.FaceImage
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.ImageSelected
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.Name
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.Position
import com.sgale.gaztelubira.core.screens.insert_player.InsertPlayerViewModel.InsertPlayerField.UseSameImg
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Default
import com.sgale.gaztelubira.core.screens.insert_player.UiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class InsertPlayerViewModel @Inject constructor(
    private val toastManager: IToastManager,
    private val showCameraUseCase: ShowCamera,
    private val showGalleryUseCase: ShowGallery,
    private val insertNewPlayerUseCase: InsertNewPlayer,
    private val getAvailableDorsals: GetAvailableDorsals
) : ViewModel() {
    enum class InsertPlayerField {
        Name, Dorsal, Position, FaceImage, BodyImage, UseSameImg, ImageSelected
    }

    private val _insertPlayerData = MutableStateFlow(InsertPlayerData())
    val data: StateFlow<InsertPlayerData> = _insertPlayerData

    private var dorsals: List<Int> = emptyList()

    private val _uiState = MutableStateFlow<UiState>(Default)
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            dorsals = withContext(Dispatchers.IO) { getAvailableDorsals() }
        }
    }

    fun getDorsals(): List<Int> = dorsals

    fun initCamera(
        permissionDeniedMsg: String,
        launchCamera: () -> Unit
    ) {
        viewModelScope.launch {
            showCameraUseCase(
                onLaunchCamera = { launchCamera() },
                onPermissionsDenied = { showToast(permissionDeniedMsg) }
            )
        }
    }

    fun initGallery(
        permissionDeniedMsg: String,
        launchGallery: () -> Unit
    ) {
        viewModelScope.launch {
            showGalleryUseCase(
                onLaunchGallery = { launchGallery() },
                onPermissionsDenied = { showToast(permissionDeniedMsg) }
            )
        }
    }

    fun insertNewPlayer(
        uploadErrorMsg: String,
        notValidPlayerMsg: String,
        onSuccess: () -> Unit
    ) {
        if (!validPlayer()) {
            insertButtonEnabled(false)
            showToast(notValidPlayerMsg) { insertButtonEnabled(true) }
            return
        }

        viewModelScope.launch {
            updateState(Loading)

            val newPlayerInserted = withContext(Dispatchers.IO) {
                insertNewPlayerUseCase(
                    player = _insertPlayerData.value.toPlayerModel(),
                    faceImg = _insertPlayerData.value.faceImage,
                    bodyImg = _insertPlayerData.value.bodyImage
                )
            }

            if (newPlayerInserted is PlayerInserted) {
                onSuccess()
            } else {
                showToast(uploadErrorMsg)
                updateState(Default)
            }
        }
    }

    fun removeImage() {
        when (getLastSelected()) {
            Face -> updateField(FaceImage, null)
            Body -> updateField(BodyImage, null)
        }
        _insertPlayerData.value = _insertPlayerData.value.copy(useSameImage = false)
    }

    fun updateState(newState: UiState) {
        _uiState.value = newState
    }

    fun updateField(field: InsertPlayerField, value: Any? = null) {
        val current = _insertPlayerData.value
        _insertPlayerData.value = when (field) {
            Name -> current.copy(name = value as String)
            Dorsal -> current.copy(dorsal = value as Int)
            Position -> current.copy(position = value as Position)
            FaceImage -> current.copy(faceImage = value as CommonImage?)
            BodyImage -> current.copy(bodyImage = value as CommonImage?)
            UseSameImg -> current.copy(useSameImage = value as Boolean)
            ImageSelected -> current.copy(lastSelected = value as PictureType)
        }

        if (field == UseSameImg && value == true) {
            useSameImage()
        } else if (field == UseSameImg && value == false) {
            removeSameImage()
        }
    }

    fun updatePicture(image: CommonImage?) {
        viewModelScope.launch {
            when (_insertPlayerData.value.lastSelected) {
                Face -> updateField(FaceImage, image)
                Body -> updateField(BodyImage, image)
            }
        }
    }

    private fun getLastSelected(): PictureType = _insertPlayerData.value.lastSelected

    private fun insertButtonEnabled(enabled: Boolean) {
        _insertPlayerData.value = _insertPlayerData.value.copy(canInsert = enabled)
    }

    private fun removeSameImage() {
        when (getLastSelected()) {
            Face -> {
                _insertPlayerData.value = _insertPlayerData.value.copy(lastSelected = Body)
                updateField(FaceImage, null)
            }
            Body -> {
                _insertPlayerData.value = _insertPlayerData.value.copy(lastSelected = Face)
                updateField(BodyImage, null)
            }
        }
    }

    private fun showToast(msg: String, onFinish: () -> Unit = {}) {
        toastManager.showToast(
            msg = msg,
            onFinish =  { onFinish() }
        )
    }

    private fun useSameImage() {
        when (getLastSelected()) {
            Face -> {
                _insertPlayerData.value = _insertPlayerData.value.copy(lastSelected = Body)
                updateField(BodyImage, _insertPlayerData.value.faceImage)
            }
            Body -> {
                _insertPlayerData.value = _insertPlayerData.value.copy(lastSelected = Face)
                updateField(FaceImage, _insertPlayerData.value.bodyImage)
            }
        }
    }

    private fun validPlayer(): Boolean {
        val player = _insertPlayerData.value
        val validDorsal = if (player.notManager()) {
            player.dorsal > 0
        } else {
            true
        }

        return player.name.isNotBlank() && validDorsal && player.position != null
    }
}
